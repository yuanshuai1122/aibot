package com.aibot.beans.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单返回体
 *
 * @author: yuanshuai
 * @create: 2023-03-30 12:10
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserOrdersVO {

  private Integer id;

  /**
   * 用户id
   */
  private String nickName;

  /**
   * 商品id
   */
  private String productName;

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
