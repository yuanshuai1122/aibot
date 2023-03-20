package com.chatgpt.tasks;

import com.chatgpt.beans.ChatLog;
import com.chatgpt.beans.ChatSensitiveLog;
import com.chatgpt.mapper.ChatLogMapper;
import com.chatgpt.mapper.ChatSensitiveLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步任务
 *
 * @author: yuanshuai
 * @create: 2023-03-15 18:25
 */
@Component
@Slf4j
public class AsyncTask {

  private final ChatLogMapper chatLogMapper;

  private final ChatSensitiveLogMapper chatSensitiveLogMapper;

  public AsyncTask(ChatLogMapper chatLogMapper, ChatSensitiveLogMapper chatSensitiveLogMapper) {
    this.chatLogMapper = chatLogMapper;
    this.chatSensitiveLogMapper = chatSensitiveLogMapper;
  }


  /**
   * 写入chatLog任务
   * @param chatLog chatLog实体
   */
  @Async("chatAsyncTaskPool")
  public void setChatLog(ChatLog chatLog) {
    chatLogMapper.insert(chatLog);
  }

  /**
   * 写入敏感词日志任务
   * @param chatSensitiveLog chatLog实体
   */
  @Async("chatAsyncTaskPool")
  public void setChatSensitiveLog(ChatSensitiveLog chatSensitiveLog) {
    chatSensitiveLogMapper.insert(chatSensitiveLog);
  }


}
