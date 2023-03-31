package com.aibot.service;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
import com.aibot.beans.entity.UserOrders;
import com.aibot.beans.vo.StatisticsVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ProductMapper;
import com.aibot.mapper.UserMapper;
import com.aibot.mapper.UserOrdersMapper;
import com.aibot.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 统计服务
 *
 * @author: yuanshuai
 * @create: 2023-03-31 22:07
 */
@Slf4j
@Service
public class StatisticsService {

  @Autowired
  private UserOrdersMapper userOrdersMapper;

  @Autowired
  private UserMapper userMapper;

  public ResponseResult<StatisticsVO> totalList() {

    StatisticsVO statistics = new StatisticsVO();

    // 查询总用户数
    Integer totalUsers = Math.toIntExact(userMapper.selectCount(null));
    statistics.setTotalUsers(totalUsers);
    // 查询今日注册用户数
    QueryWrapper<User> userWrapper = new QueryWrapper<>();
    userWrapper.lambda().between(User::getCreateTime, DateUtils.getTimesmorning(), DateUtils.getTimesnight());
    Integer todayUsers = Math.toIntExact(userMapper.selectCount(userWrapper));
    statistics.setTodayUsers(todayUsers);

    // 查询订单总数
    Integer totalOrders = Math.toIntExact(userOrdersMapper.selectCount(null));
    statistics.setTotalOrders(totalOrders);
    // 查询今日订单数
    QueryWrapper<UserOrders> ordersWrapper = new QueryWrapper<>();
    ordersWrapper.lambda().between(UserOrders::getOrderTime, DateUtils.getTimesmorning(), DateUtils.getTimesnight());
    Integer todayOrders = Math.toIntExact(userOrdersMapper.selectCount(ordersWrapper));
    statistics.setTodayOrders(todayOrders);

    // 查询总收款金额
    BigDecimal totalAmount = userOrdersMapper.selectTotalAmount();
    statistics.setTotalAmount(totalAmount);
    BigDecimal todayAmount = userOrdersMapper.selectAmountTime(DateUtils.getTimesmorning(), DateUtils.getTimesnight());
    statistics.setTodayAmount(todayAmount);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", statistics);

  }
}
