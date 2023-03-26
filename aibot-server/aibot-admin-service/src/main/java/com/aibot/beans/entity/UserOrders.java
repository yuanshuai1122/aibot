package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单
 *
 * @author: yuanshuai
 * @create: 2023-03-23 18:23
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserOrders {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 用户id
   */
  private Integer userId;

  /**
   * 商品id
   */
  private Integer productId;

  /**
   * 数量
   */
  private Integer quantity;

  /**
   * 金额
   */
  private BigDecimal totalAmount;

  /**
   * 订单状态
   */
  private String orderStatus;

  /**
   * 订单时间
   */
  private Date orderTime;

  /**
   * 支付时间
   */
  private Date payTime;

}
