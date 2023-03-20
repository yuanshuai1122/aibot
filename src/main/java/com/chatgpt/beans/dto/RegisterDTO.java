package com.chatgpt.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册DTO
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:41
 */
@Data
public class RegisterDTO {

  @NotBlank
  private String account;

  @NotBlank
  private String password;
}
