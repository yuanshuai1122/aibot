package com.aibot.beans.dto.entity.ChatResult;

import lombok.Data;

/**
 * Choices
 *
 * @author: aabb
 * @create: 2023-03-08 21:10
 */
@Data
public class Choices {

  private String finishReason;
  private Integer index;
  private Message message;

}
