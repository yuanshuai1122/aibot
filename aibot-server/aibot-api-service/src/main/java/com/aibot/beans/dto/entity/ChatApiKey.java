package com.aibot.beans.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * api的key管理
 *
 * @author: aabb
 * @create: 2023-04-09 17:57
 */
@Data
public class ChatApiKey {

  @TableId(type = IdType.AUTO)
  private Integer id;

  private String apiKey;

  private String account;

  private Integer status;

}
