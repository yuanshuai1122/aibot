package com.aibot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aibot.beans.entity.ChatSuccessLog;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * chat日志mapper
 *
 * @author: aabb
 * @create: 2023-03-14 12:18
 */
@Mapper
public interface ChatSuccessLogMapper extends MPJBaseMapper<ChatSuccessLog> {


  //@Insert("insert into chat_log(user_id, role, conversation_id, content, create_time) " +
  //        "values(#{userId}, #{role}, #{conversationId}, #{content}, #{createTime})")
  //Integer insertChatLog(ChatLog chatLog);

}
