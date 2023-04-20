package com.aibot.beans.vo;

import com.aibot.constants.RegConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 发短信dto
 *
 * @author: aabb
 * @create: 2023-03-27 11:48
 */
@Data
public class SmsSendDTO {

  @Pattern(regexp = RegConstants.PHONE_REG)
  private String phone;

  @NotBlank
  private String type;

}
