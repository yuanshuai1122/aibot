package com.aibot.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 实名DTO
 *
 * @author: yuanshuai
 * @create: 2023-03-29 09:36
 */
@Data
public class RealNameDTO {

  /**
   * 真实姓名
   */
  @NotBlank
  private String trueName;

  /**
   * 身份证号
   */
  @NotBlank
  private String cerNumber;

}
