package com.aibot.beans.dto;

import com.aibot.constants.RegConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 登录DTO
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:41
 */
@Data
public class LoginDTO {

  @NotBlank
  private String account;

  private String password;

  private String verifyCode;

  @NotBlank
  private String type;
}
