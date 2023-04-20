package com.aibot.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * chat的api列表
 *
 * @author: aabb
 * @create: 2023-03-24 20:51
 */
@Data
public class ChatApiKey {

  /**
   * 主键id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * apiKey
   */
  private String apiKey;
}
