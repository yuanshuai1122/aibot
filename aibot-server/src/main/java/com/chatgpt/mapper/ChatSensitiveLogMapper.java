package com.chatgpt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chatgpt.beans.entity.ChatSensitiveLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatSensitiveLogMapper extends BaseMapper<ChatSensitiveLog> {
}
