package com.aibot.beans.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.util.Date;

/**
 * 聊天信息实体
 *
 * @author: aabb
 * @create: 2023-03-14 11:58
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatSuccessLog {

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
   * 角色
   */
  private String role;

  /**
   * 消息唯一id
   */
  private String messageId;

  /**
   * 对话id
   */
  private String conversationId;

  /**
   * 对话内容
   */
  private String content;

  /**
   * 创建时间
   */
  private Date createTime;
}
