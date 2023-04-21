package com.aibot.service;

import com.aibot.beans.dto.entity.ChatApiKey;
import com.aibot.beans.dto.entity.ChatFailureLog;
import com.aibot.beans.dto.entity.ChatResult.ChatResult;
import com.aibot.beans.dto.entity.ChatStreamResult.ChatStreamResult;
import com.aibot.beans.dto.entity.ChatSuccessLog;
import com.aibot.beans.dto.entity.ResponseResult;
import com.aibot.beans.dto.entity.chatProcess.ChatProcess;
import com.aibot.beans.dto.entity.chatProcess.ChatPrompt;
import com.aibot.beans.dto.vo.ChatStreamVO;
import com.aibot.config.ChatExecutorConfig;
import com.aibot.config.OkHttpClientSingleton;
import com.aibot.constants.ApiBaseUrl;
import com.aibot.enums.ChatRoleEnum;
import com.aibot.enums.ResultCode;
import com.aibot.mapper.ChatApiKeyMapper;
import com.aibot.mapper.ChatSuccessLogMapper;
import com.aibot.tasks.AsyncTask;
import com.aibot.utils.OkHttpUtils;
import com.aibot.utils.RequestUtils;
import com.aibot.utils.ValueUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

  @Autowired
  private AsyncTask asyncTask;

  @Autowired
  private ChatExecutorConfig chatExecutorConfig;


  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ChatSuccessLogMapper chatSuccessLogMapper;

  @Autowired
  private HttpServletRequest request;
  
  private static final ConcurrentHashMap<Integer, String> apiMap = new ConcurrentHashMap<>(16);



  /**
   * 聊天（stream版）
   * @param signKey 聊天key
   * @return stream
   */
  public SseEmitter chatStream(String signKey) {

    // 从缓存中获取请求消息
    String value = redisTemplate.opsForValue().get(signKey);
    if (StringUtils.isBlank(value)) {
      log.info("缓存中没查询到请求消息, key:{}", signKey);
      return null;
    }

    // new SseEmitter timeout设置为0表示不超时
    SseEmitter emitter = new SseEmitter();
    log.info("创建SseEmitter, {}", emitter);

    // 获取随机一条key
    if (apiMap.size() == 0) {
      QueryWrapper<ChatApiKey> wrapper = new QueryWrapper<>();
      wrapper.lambda().ne(ChatApiKey::getStatus, 1);
      List<ChatApiKey> chatApiKeys = chatApiKeyMapper.selectList(wrapper);
      if (chatApiKeys.isEmpty()) {
        log.info("没有可用apiKey");
        return null;
      }
      for (ChatApiKey chatApiKey : chatApiKeys) {
        apiMap.put(chatApiKey.getId(), chatApiKey.getApiKey());
      }
    }
    // 获取随机key
    Integer [] keys = apiMap.keySet().toArray(new Integer[0]);
    int random = (int) (Math.random()*(keys.length));
    Integer randomKey = keys[random];
    String apiKey = apiMap.get(randomKey);


    log.info("开始构造流式请求，id:{}, apiKey:{}", randomKey, apiKey);

    // 构建请求头
    Map<String, String> headers = RequestUtils.buildRequestHeaders(apiKey);
    log.info("构建请求头, headers: {}", headers);

    // 构建请求体
    ChatSuccessLog chatSuccessLog = new Gson().fromJson(value, ChatSuccessLog.class);
    List<ChatPrompt> prompts =new Gson().fromJson(chatSuccessLog.getContent(), new TypeToken<List<ChatPrompt>>() {}.getType());
    log.info("prompts:{}", prompts);
    Map<String, Object> data = RequestUtils.buildRequestParams(value);
    data.put("messages", prompts);
    log.info("构建请求体, data: {}", data);
    // 删除缓存
    redisTemplate.delete(signKey);

    // 静态okhttpClient
    OkHttpClient.Builder builder = OkHttpClientSingleton.getInstance().newBuilder();
    builder.connectTimeout(Duration.ofSeconds(300));
    builder.readTimeout(Duration.ofSeconds(300));
    builder.writeTimeout(Duration.ofSeconds(300));
    OkHttpClient okHttpClient = builder.build();

    Request req = new Request.Builder()
            .url(ApiBaseUrl.BASE_CHAT_URL)
            .headers(Headers.of(headers))
            .post(RequestBody.create(new Gson().toJson(data), MediaType.parse("application/json")))
            .build();
    log.info("构建请求request: {}", req);

    // 新建线程发送 SSE 事件流数据
    chatExecutorConfig.chatAsyncTaskPool().execute(() -> {
      try (Response response = okHttpClient.newCall(req).execute()) {
        log.info("开始推流......");
        ResponseBody responseBody = response.body();
        if (response.isSuccessful() && responseBody != null) {
          // 读取 SSE 事件流数据并发送给客户端
          while (!responseBody.source().exhausted()) {
            String line = responseBody.source().readUtf8LineStrict();
            // 封装 SSE 事件流数据并发送给客户端
            if (line.startsWith("data:")) {
              line = line.substring(6);
              System.out.println(line);

              if (line.contains("[DONE]")) {
                continue;
              }

              // json转实体
              ChatStreamResult chatStreamResult = new Gson().fromJson(line, ChatStreamResult.class);

              if ("assistant".equals(chatStreamResult.getChoices().get(0).getDelta().getRole())) {
                continue;
              }

              // 收集返回值
              String chatItem = chatStreamResult.getChoices().get(0).getDelta().getContent();
              // 构造返回体
              ChatStreamVO chatStream = new ChatStreamVO(chatItem);

              emitter.send(SseEmitter.event().data(chatStream));
            }
          }
        } else {
          ChatFailureLog chatFailureLog = new ChatFailureLog(null, chatSuccessLog.getUserId(), chatSuccessLog.getMessageId(), chatSuccessLog.getConversationId(), "CHAT响应失败或响应为空", new Date());
          asyncTask.setChatLogFailure(chatFailureLog);
          emitter.completeWithError(new RuntimeException("CHAT响应失败或响应为空"));
        }

      } catch (IOException e) {
        log.info("请求chat流式接口发生异常, e: {}", e.toString());
        // 发送 SSE 事件流数据出现异常时需要调用 completeWithError() 方法
        emitter.completeWithError(new RuntimeException("请求服务器发生异常，请重试"));
        ChatFailureLog chatFailureLog = new ChatFailureLog(null, chatSuccessLog.getUserId(), chatSuccessLog.getMessageId(), chatSuccessLog.getConversationId(), "请求服务器发生异常，请重试", new Date());
        asyncTask.setChatLogFailure(chatFailureLog);
        throw new RuntimeException("请求服务器发生异常，请重试");
      }finally {
        log.info("推流顺利结束，结束sse emitter: {}", emitter);
        emitter.complete();
      }
    });

    // 返回 SseEmitter 实例
    return emitter;
  }



  public ResponseResult<ChatResult> chatCommon(ChatProcess dto) {

    try {
      log.info("用户: {}",  dto.getPrompt());
      OkHttpUtils client = OkHttpUtils.builder();

      client.url(ApiBaseUrl.BASE_CHAT_URL);
      client.addHeader("Content-Type", "application/json");
      client.addHeader("Authorization", "Bearer " + "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");
      client.addParam("model", "gpt-3.5-turbo");
      client.addParam("messages", dto.getPrompt());
      String sync = client.post(true).sync();
      ChatResult chatResult = JSON.parseObject(sync, ChatResult.class);
      System.out.println(chatResult);


      // 插入用户提问到数据库
      log.info("user: {}", dto.getPrompt());
      ChatSuccessLog userChat = new ChatSuccessLog(null, 1, ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), chatResult.getId(), new Gson().toJson(dto.getPrompt()), new Date());
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


}
