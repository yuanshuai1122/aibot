package com.aibot.beans.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体
 *
 * @author: aabb
 * @create: 2023-03-23 18:05
 */
@Data
public class ProductVO {

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
   * 商品背景图
   */
  private String imgUrl;

  /**
   * 商品剩余数量
   */
  private Integer count;

  /**
   * 商品描述
   */
  private String productDescription;

}
