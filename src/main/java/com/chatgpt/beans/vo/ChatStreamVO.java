package com.chatgpt.beans.vo;

import lombok.*;

/**
 * Stream版本返回体
 *
 * @author: yuanshuai
 * @create: 2023-03-16 21:27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatStreamVO {

  /**
   * 消息唯一id
   */
  private String messageId;

  /**
   * 会话id
   */
  private String conversationId;

  /**
   * 会话内容
   */
  private String content;

}
