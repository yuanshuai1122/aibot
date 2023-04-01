package com.aibot.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 统计返回体
 *
 * @author: yuanshuai
 * @create: 2023-03-31 22:02
 */
@Data
public class StatisticsVO {

  /**
   * 今日注册用户
   */
  private Integer totalUsers;

  /**
   * 总注册用户数
   */
  private Integer todayUsers;

  /**
   * 总订单数
   */
  private Integer totalOrders;

  /**
   * 今日订单数
   */
  private Integer todayOrders;

  /**
   * 总收款金额
   */
  private BigDecimal totalAmount;

  /**
   * 今日收款金额
   */
  private BigDecimal todayAmount;

}
