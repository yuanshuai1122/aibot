package com.chatgpt.beans.ChatResult;

import lombok.Data;

import java.util.List;

/**
 * chat返回值实体
 *
 * @author: yuanshuai
 * @create: 2023-03-08 20:49
 */
@Data
public class ChatResult {

  private String id;

  private String object;

  private Integer created;

  private String model;

  private Usage usage;

  private List<Choices> choices;

}
