package com.aibot.beans.dto.entity.ChatStreamResult;

import lombok.Data;

/**
 * Choices
 *
 * @author: aabb
 * @create: 2023-03-08 22:15
 */
@Data
public class Choices {

  private Integer index;

  private String finishReason;

  private Delta delta;

}
