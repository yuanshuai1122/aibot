package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户钱包
 *
 * @author: aabb
 * @create: 2023-03-30 12:34
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserMoney {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 用户id
   */
  private Integer userId;

  /**
   * 余额
   */
  private BigDecimal amount;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date updateTime;
}
