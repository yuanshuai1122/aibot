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

  /**
   * 账号
   */
  @NotBlank
  private String account;

  /**
   * 密码
   */
  @NotBlank
  private String password;

  /**
   * 邀请码
   */
  private String shareCode;

  /**
   * 短信验证码
   */
  private String verifyCode;

}
