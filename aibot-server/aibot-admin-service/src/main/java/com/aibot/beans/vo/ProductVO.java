package com.aibot.beans.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体
 *
 * @author: yuanshuai
 * @create: 2023-03-23 18:05
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductVO {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 租户名称
   */
  private String tenantName;

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
   * 是否上架
   */
  private Integer putStatus;

  /**
   * 商品描述
   */
  private String productDescription;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date updateTime;

}
