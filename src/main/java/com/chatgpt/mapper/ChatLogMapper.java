package com.chatgpt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chatgpt.beans.ChatLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * chat日志mapper
 *
 * @author: yuanshuai
 * @create: 2023-03-14 12:18
 */
@Mapper
public interface ChatLogMapper extends BaseMapper<ChatLog> {


  //@Insert("insert into chat_log(user_id, role, conversation_id, content, create_time) " +
  //        "values(#{userId}, #{role}, #{conversationId}, #{content}, #{createTime})")
  //Integer insertChatLog(ChatLog chatLog);

}
