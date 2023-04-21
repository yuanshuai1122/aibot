package com.aibot.beans.dto.vo.journey;

import lombok.Data;

/**
 * 创建journey
 *
 * @author: aabb
 * @create: 2023-04-04 21:13
 */
@Data
public class JourneyCreateVO {

    private String completedAt;
    private String createdAt;
    private String error;
    private String id;
    private Input input;
    private String logs;
    private Metrics metrics;
    private String output;
    private String startedAt;
    private String status;
    private Urls urls;
    private String version;
    private String webhookCompleted;

}
