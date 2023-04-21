package com.aibot.beans.dto.vo.journey;

import lombok.Data;

/**
 * Journey返回体
 *
 * @author: aabb
 * @create: 2023-04-04 21:24
 */
@Data
public class JourneyResultVO {

  private String id;

  private Input input;

  private String output;

  private String status;
}
