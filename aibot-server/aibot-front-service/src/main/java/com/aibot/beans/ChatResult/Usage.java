package com.aibot.beans.ChatResult;

import lombok.Data;

/**
 * Usage
 *
 * @author: aabb
 * @create: 2023-03-08 21:09
 */
@Data
public class Usage {

  private Integer promptTokens;

  private Integer completionTokens;

  private Integer totalTokens;
}
