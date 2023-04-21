package com.aibot.beans.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.Date;

/**
 * chat失败日志表
 *
 * @author: aabb
 * @create: 2023-03-24 20:35
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatFailureLog {

  /**
   * 主键id
   */
  @TableId(type = IdType.AUTO)
  private Integer id;

  /**
   * 用户id
   */
  private Integer userId;


  /**
   * 消息唯一id
   */
  private String messageId;

  /**
   * 对话id
   */
  private String conversationId;

  /**
   * 原因
   */
  private String reasons;

  /**
   * 创建时间
   */
  private Date createTime;
}
