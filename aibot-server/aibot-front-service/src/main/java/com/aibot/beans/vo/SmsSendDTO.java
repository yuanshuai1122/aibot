package com.aibot.beans.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 发短信dto
 *
 * @author: yuanshuai
 * @create: 2023-03-27 11:48
 */
@Data
public class SmsSendDTO {

  @NotBlank
  private String phone;

}
