package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 分销配置表
 *
 * @author: yuanshuai
 * @create: 2023-03-31 18:12
 */
@Data
public class DistributionConfig {


  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 租户id
   */
  private Integer tenantId;

  /**
   * 等级
   */
  private Integer level;

  /**
   * 等级名称
   */
  private String name;

  /**
   * 比率
   */
  private Integer rate;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date updateTime;
}
