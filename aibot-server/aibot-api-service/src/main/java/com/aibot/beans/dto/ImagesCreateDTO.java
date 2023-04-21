package com.aibot.beans.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 生成图片dto
 *
 * @author: aabb
 * @create: 2023-03-23 14:27
 */
@Data
public class ImagesCreateDTO {

  @NotBlank
  private String prompt;

}
