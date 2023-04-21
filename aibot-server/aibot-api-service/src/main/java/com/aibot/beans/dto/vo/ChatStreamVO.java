package com.aibot.beans.dto.vo;

import lombok.*;

/**
 * Stream版本返回体
 *
 * @author: aabb
 * @create: 2023-03-16 21:27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatStreamVO {
  /**
   * 会话内容
   */
  private String content;

}
