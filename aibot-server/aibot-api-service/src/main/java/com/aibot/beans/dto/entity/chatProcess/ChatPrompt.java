package com.aibot.beans.dto.entity.chatProcess;

import lombok.Data;

/**
 * chat流式dto
 *
 * @author: aabb
 * @create: 2023-03-06 17:31
 */
@Data
public class ChatPrompt {

  /**
   * 角色
   */
  private String role;


  /**
   * 内容
   */
  private String content;

}
