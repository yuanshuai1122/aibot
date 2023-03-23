package com.chatgpt.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品实体
 *
 * @author: yuanshuai
 * @create: 2023-03-23 18:05
 */
@Data
public class Product {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 商品名称
   */
  private String productName;

  /**
   * 商品价格
   */
  private BigDecimal productPrice;

  /**
   * 商品描述
   */
  private String productDescription;

}
