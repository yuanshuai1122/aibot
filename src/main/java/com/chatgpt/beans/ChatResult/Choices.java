package com.chatgpt.beans.ChatResult;

import lombok.Data;

/**
 * Choices
 *
 * @author: yuanshuai
 * @create: 2023-03-08 21:10
 */
@Data
public class Choices {

  private String finishReason;
  private Integer index;
  private Message message;

}
