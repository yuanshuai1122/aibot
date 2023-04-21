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
public class JourneyCreateDTO {

  /**
   * Default value: mdjrny-v4 style a highly detailed matte painting of a man on a hill watching a rocket launch in the distance by studio ghibli,
   * makoto shinkai, by artgerm, by wlop, by greg rutkowski, volumetric lighting, octane render, 4 k resolution, trending on artstation, masterpiece
   */
  @NotBlank
  private String prompt;

  /**
   * Width of output image. Maximum size is 1024x768 or 768x1024 because of memory limits
   * Allowed values:128, 256, 512, 768, 1024
   * Default value: 512
   */
  private Integer width;

  /**
   * Height of output image. Maximum size is 1024x768 or 768x1024 because of memory limits
   * Allowed values:128, 256, 512, 768, 1024
   * Default value: 512
   */
  private Integer height;

  /**
   * Number of images to output
   * Allowed values:1, 4
   * Default value: 1
   */
  private Integer numOutputs;

  /**
   * Number of denoising steps
   * Default value: 50
   */
  private Integer numInferenceSteps;

  /**
   * Scale for classifier-free guidance
   * Default value: 6
   */
  private Integer guidanceScale;

  /**
   * Random seed. Leave blank to randomize the seed
   */
  private Integer seed;




}
