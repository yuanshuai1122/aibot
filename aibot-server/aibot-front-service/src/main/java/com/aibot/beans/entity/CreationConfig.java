package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 创作配置管理
 *
 * @author: aabb
 * @create: 2023-04-01 22:07
 */
@Data
public class CreationConfig {

  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 租户id
   */
  private Integer tenantId;

  /**
   * 类型ID
   */
  private Integer typeId;

  /**
   * 标题
   */
  private String title;

  /**
   * 描述
   */
  private String des;

  /**
   * 背景图
   */
  private String imgUrl;

  /**
   * 使用次数
   */
  private Integer useCount;

  /**
   * 模板
   */
  private String template;

  /**
   * 状态
   */
  private Integer status;

  /**
   * 字数
   */
  private Integer wordCount;

  /**
   * 关键字
   */
  private String keywords;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date updateTime;

}
