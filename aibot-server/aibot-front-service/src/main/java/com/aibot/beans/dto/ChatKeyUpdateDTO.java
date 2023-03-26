package com.aibot.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新chat的key
 *
 * @author: yuanshuai
 * @create: 2023-03-25 22:28
 */
@Data
public class ChatKeyUpdateDTO {

  /**
   * 用户key id
   */
  @NotBlank
  private String userKey;

  /**
   * 是否激活
   */
  private String activeStatus;

  /**
   * 体验/包月...
   */
  private String keyType;

}
