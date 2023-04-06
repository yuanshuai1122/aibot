package com.aibot.beans.dto;

import com.aibot.constants.RegConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
  @Pattern(regexp = RegConstants.PHONE_REG)
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
  @NotBlank
  private String verifyCode;

}
