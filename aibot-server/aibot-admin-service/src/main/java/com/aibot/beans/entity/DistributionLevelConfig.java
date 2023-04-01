package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 分销等级配置
 *
 * @author: yuanshuai
 * @create: 2023-03-31 18:16
 */
@Data
public class DistributionLevelConfig {


  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 租户id
   */
  private Integer tenantId;

  /**
   * 能开最大等级
   */
  private Integer maxLevel;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date updateTime;
}
