package com.aibot.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 下单商品dto
 *
 * @author: aabb
 * @create: 2023-03-23 17:55
 */
@Data
public class OrderProductDTO {

  /**
   * 商品id
   */
  private Integer productId;

  /**
   * 数量
   */
  private Integer count;


  /**
   * 支付渠道
   */
  private String channel;

}
