package com.aibot.beans.dto;

import lombok.Data;

/**
 * 添加渠道商
 *
 * @author: aabb
 * @create: 2023-04-09 21:41
 */
@Data
public class AddChannelDTO {

  /**
   * 分享码
   */
  private String shareCode;

  /**
   * 租户域名
   */
  private String tenantHost;

}
