package com.aibot.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 商品实体
 *
 * @author: aabb
 * @create: 2023-03-23 18:05
 */
@Data
public class CreateProductDTO {

  /**
   * 商品名称
   */
  @NotBlank
  private String productName;

  /**
   * 底价
   */
  private BigDecimal miniPrice;

  /**
   * 商品价格
   */
  private BigDecimal productPrice;

  /**
   * 商品背景图
   */
  @NotBlank
  private String imgUrl;

  /**
   * 商品剩余数量
   */
  private Integer count;

  /**
   * 是否上架
   */
  private Integer putStatus;

  /**
   * 商品描述
   */
  @NotBlank
  private String productDescription;

}
