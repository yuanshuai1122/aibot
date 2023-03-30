package com.aibot.service;

import com.aibot.beans.entity.*;
import com.aibot.beans.vo.OrderStatus;
import com.aibot.beans.vo.UserOrdersVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.UserOrdersMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 订单服务
 *
 * @author: yuanshuai
 * @create: 2023-03-30 12:07
 */
@Service
@Slf4j
public class OrderService {

  @Autowired
  private UserOrdersMapper userOrdersMapper;


  public ResponseResult<HashMap<String, Object>> orderList(String nickName, String productName, String orderStatus, Integer pageNum, Integer pageSize) {

    MPJLambdaWrapper<UserOrders> wrapper = new MPJLambdaWrapper<UserOrders>()
            .select(UserOrders::getId, UserOrders::getQuantity, UserOrders::getTotalAmount, UserOrders::getOrderStatus, UserOrders::getOrderTime, UserOrders::getPayTime)
            .innerJoin(UserInfo.class, UserInfo::getUserId, UserOrders::getUserId)
            .select(UserInfo::getNickName)
            .innerJoin(Product.class, Product::getId, UserOrders::getProductId)
            .select(Product::getProductName)
            .like(StringUtils.isNotBlank(nickName), UserInfo::getNickName, nickName)
            .like(StringUtils.isNotBlank(productName), Product::getProductName, productName)
            .eq(StringUtils.isNotBlank(orderStatus), UserOrders::getOrderStatus, orderStatus);

    Page<UserOrdersVO> listPage = userOrdersMapper.selectJoinPage(new Page<>(pageNum, pageSize), UserOrdersVO.class, wrapper);
    HashMap<String, Object> map = new HashMap<>();
    map.put("orderList", listPage.getRecords());
    map.put("totalCount", listPage.getTotal());

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", map);

  }


  public ResponseResult<List<OrderStatus>> orderStatusList() {

    ArrayList<OrderStatus> orderStatuses = new ArrayList<>();

    OrderStatus status1 = new OrderStatus("待支付", "PENDING");
    OrderStatus status2 = new OrderStatus("已支付", "PAID");
    OrderStatus status3 = new OrderStatus("已完成", "COMPLETED");
    OrderStatus status4 = new OrderStatus("已结算", "SETTLED");
    OrderStatus status5 = new OrderStatus("已取消", "CANCELED");
    orderStatuses.add(status1);
    orderStatuses.add(status2);
    orderStatuses.add(status3);
    orderStatuses.add(status4);
    orderStatuses.add(status5);


    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", orderStatuses);

  }
}
