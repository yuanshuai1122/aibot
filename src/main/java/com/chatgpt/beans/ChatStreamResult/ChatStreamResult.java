package com.chatgpt.beans.ChatStreamResult;

import lombok.Data;

import java.util.List;

/**
 * ChatStreamResult
 *
 * @author: yuanshuai
 * @create: 2023-03-08 22:14
 */
@Data
public class ChatStreamResult {

  private String id;

  private String object;

  private Integer created;

  private String model;

  private List<Choices> choices;

}
