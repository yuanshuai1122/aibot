package com.chatgpt.beans.dto;

import lombok.Data;

/**
 * chat普通版
 *
 * @author: yuanshuai
 * @create: 2023-03-15 09:47
 */
@Data
public class ChatCommonDTO {

  private Integer id;

  private String conversationId;

  private String chatMessage;

  private String prompt;

}
