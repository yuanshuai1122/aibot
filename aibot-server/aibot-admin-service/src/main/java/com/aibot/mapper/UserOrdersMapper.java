package com.aibot.mapper;

import com.aibot.beans.entity.UserOrders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单mapper
 *
 * @author: aabb
 * @create: 2023-03-23 21:18
 */
@Mapper
public interface UserOrdersMapper extends MPJBaseMapper<UserOrders> {


  /**
   * 查询总订单金额
   * @return 总订单金额
   */
  @Select("select IFNULL(sum(total_amount),0) AS totalAmount from user_orders")
  BigDecimal selectTotalAmount();


  /**
   * 查询某个时间段订单金额
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return 订单金额
   */
  @Select("select IFNULL(sum(total_amount),0) AS totalAmount from user_orders where order_time >= #{startTime} and order_time < #{endTime}")
  BigDecimal selectAmountTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
