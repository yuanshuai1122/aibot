package com.aibot.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录DTO
 *
 * @author: aabb
 * @create: 2023-03-20 11:41
 */
@Data
public class LoginDTO {

  @NotBlank
  private String account;

  @NotBlank
  private String password;
}
