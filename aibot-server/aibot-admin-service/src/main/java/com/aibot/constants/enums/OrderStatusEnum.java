package com.aibot.constants.enums;

import lombok.Getter;

/**
 * 订单状态
 *
 * @author: yuanshuai
 * @create: 2023-03-29 18:36
 */
@Getter
public enum OrderStatusEnum {


  /**
   * 超级管理员
   */
  PENDING("PENDING", "待支付"),

  /**
   * 已支付
   */
  PAID("PAID", "已支付"),

  /**
   * 已完成
   */
  COMPLETED("COMPLETED", "已完成"),

  /**
   *
   */
  SETTLED("SETTLED", "已结算"),




  CANCELED("CANCELED", "已取消"),



  ;



  private String status;

  private String des;

  private OrderStatusEnum() {

  }

  OrderStatusEnum(String status, String des) {
    this.status = status;
    this.des = des;
  }



}
