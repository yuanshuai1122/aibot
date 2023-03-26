package com.chatgpt.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 下单商品dto
 *
 * @author: yuanshuai
 * @create: 2023-03-23 17:55
 */
@Data
public class OrderProductDTO {

  /**
   * 商品id
   */
  @NotBlank
  private Integer productId;

  /**
   * 数量
   */
  @NotBlank
  private Integer count;

}
