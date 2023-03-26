package com.aibot.beans.dto.chatProcess;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * chat流式dto
 *
 * @author: yuanshuai
 * @create: 2023-03-06 17:31
 */
@Data
public class ChatProcess {

  private String conversationId;

  @NotBlank
  private String prompt;

  @NotBlank
  private String chatKey;

}
