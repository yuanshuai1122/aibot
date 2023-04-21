package com.aibot.service;

import com.alibaba.fastjson2.JSON;
import com.aibot.beans.ChatResult.ChatResult;
import com.aibot.beans.ChatStreamResult.ChatStreamResult;
import com.aibot.beans.dto.ChatCommonDTO;
import com.aibot.beans.dto.chatProcess.ChatProcess;
import com.aibot.beans.entity.ChatApiKey;
import com.aibot.beans.entity.ChatFailureLog;
import com.aibot.beans.entity.ChatSuccessLog;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.ChatStreamVO;
import com.aibot.config.AsyncTaskExecutePool;
import com.aibot.config.OkHttpClientSingleton;
import com.aibot.constants.ApiBaseUrl;
import com.aibot.constants.ApiKeyConfig;
import com.aibot.constants.enums.ChatRoleEnum;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ChatApiKeyMapper;
import com.aibot.mapper.ChatSuccessLogMapper;
import com.aibot.tasks.AsyncTask;
import com.aibot.utils.OkHttpUtils;
import com.aibot.utils.RequestUtils;
import com.aibot.utils.ValueUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * chatGPT api版本服务
 *
 * @author: aabb
 * @create: 2023-03-08 16:55
 */
@Service
@Slf4j
public class ChatService {

  @Autowired
  private ChatApiKeyMapper chatApiKeyMapper;

  /**
   * String Buffer
   */
  private volatile StringBuffer chatMessageBuffer = new StringBuffer("");

  /**
   * 会话id
   */
  private volatile String conversionId;

  /**
   * 消息id
   */
  private volatile String messageId;

  @Autowired
  private AsyncTaskExecutePool asyncTaskExecutePool;

  @Autowired
  private AsyncTask asyncTask;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ChatSuccessLogMapper chatSuccessLogMapper;

  @Autowired
  private HttpServletRequest request;


  public ResponseResult<ChatResult> chatCommon(ChatCommonDTO dto) {

    try {
      log.info("用户: {}",  dto.getPrompt());
      OkHttpUtils client = OkHttpUtils.builder();

      client.url(ApiBaseUrl.BASE_CHAT_URL);
      client.addHeader("Content-Type", "application/json");
      client.addHeader("Authorization", "Bearer " + ApiKeyConfig.API_KEY1);
      Map<String, Object> message = new HashMap<>();
      message.put("role", "user");
      message.put("content", dto.getPrompt());
      client.addParam("model", "gpt-3.5-turbo");
      client.addParam("messages", Collections.singletonList(message));
      String sync = client.post(true).sync();
      ChatResult chatResult = JSON.parseObject(sync, ChatResult.class);
      System.out.println(chatResult);


      // 插入用户提问到数据库
      log.info("user: {}", dto.getPrompt());
      ChatSuccessLog userChat = new ChatSuccessLog(null, 1, ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), chatResult.getId(), dto.getPrompt(), new Date());
      chatSuccessLogMapper.insert(userChat);

      // 插入到数据库
      log.info("chat: {}",  chatResult.getChoices().get(0).getMessage().getContent());
      ChatSuccessLog chatSuccessLog = new ChatSuccessLog(null, 1, ChatRoleEnum.ASSISTANT.getRole(), chatResult.getId(),  chatResult.getId(), chatResult.getChoices().get(0).getMessage().getContent(), new Date());
      chatSuccessLogMapper.insert(chatSuccessLog);

      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "请求成功", chatResult);
    }catch (Exception e) {
      log.info(e.toString());
      return null;
    }

  }


  /**
   * chat签名
   * @param process 请求实体
   * @return
   */
  public ResponseResult<String> chatSign(ChatProcess process) {

    // 校验敏感词
    //boolean contains = SensitiveWordHelper.contains(process.getPrompt());
    //if (contains) {
    //  ChatSensitiveLog sensitiveLog = new ChatSensitiveLog(null, chatKey.getId(), ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), ValueUtils.getUUID(), process.getPrompt(), new Date());
    //  asyncTask.setChatSensitiveLog(sensitiveLog);
    //  return new ResponseResult<>(ResultCode.NOT_PERMISSION.getCode(), "请勿输入非法词汇");
    //}

    // 加密key
    String signKey = ValueUtils.getUUID();

    // 写日志数据库
    ChatSuccessLog chatSuccessLog = new ChatSuccessLog(null, Integer.parseInt(request.getAttribute("id").toString()), ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), signKey, new Gson().toJson(process.getPrompt()), new Date());
    asyncTask.setChatLog(chatSuccessLog);
    // 放入缓存队列
    redisTemplate.opsForValue().set(signKey, new Gson().toJson(chatSuccessLog), 50, TimeUnit.MINUTES);

    // 返回key
    log.info("请求chatSign结束, 返回值key: {}", signKey);
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", signKey);
  }


}
