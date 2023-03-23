package com.chatgpt.service;

import com.alibaba.fastjson2.JSON;
import com.chatgpt.beans.entity.ChatLog;
import com.chatgpt.beans.ChatResult.ChatResult;
import com.chatgpt.beans.entity.ChatSensitiveLog;
import com.chatgpt.beans.ChatStreamResult.ChatStreamResult;
import com.chatgpt.beans.entity.ResponseResult;
import com.chatgpt.beans.dto.ChatCommonDTO;
import com.chatgpt.beans.dto.chatProcess.ChatProcess;
import com.chatgpt.beans.vo.ChatStreamVO;
import com.chatgpt.config.AsyncTaskExecutePool;
import com.chatgpt.constants.enums.ChatRoleEnum;
import com.chatgpt.constants.enums.ResultCode;
import com.chatgpt.mapper.ChatLogMapper;
import com.chatgpt.tasks.AsyncTask;
import com.chatgpt.utils.OkHttpUtils;
import com.chatgpt.utils.ValueUtils;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * chatGPT api版本服务
 *
 * @author: yuanshuai
 * @create: 2023-03-08 16:55
 */
@Service
@Slf4j
public class ChatApiService {

  private final String chatUrl = "https://api.openai.com/v1/chat/completions";

  private final String API_KEY = "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";

  private volatile StringBuffer chatMessageBuffer = new StringBuffer("");

  private volatile String conversionId;

  private volatile String messageId;

  @Autowired
  private AsyncTaskExecutePool asyncTaskExecutePool;

  @Autowired
  private AsyncTask asyncTask;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ChatLogMapper chatLogMapper;



  public ResponseResult<ChatResult> chatCommon(ChatCommonDTO dto) {

    try {
      log.info("用户: {}",  dto.getPrompt());
      OkHttpUtils client = OkHttpUtils.builder();

      client.url(chatUrl);
      client.addHeader("Content-Type", "application/json");
      client.addHeader("Authorization", "Bearer " + API_KEY);
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
      ChatLog userChat = new ChatLog(null, 1, ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), chatResult.getId(), dto.getPrompt(), new Date());
      chatLogMapper.insert(userChat);

      // 插入到数据库
      log.info("chat: {}",  chatResult.getChoices().get(0).getMessage().getContent());
      ChatLog chatLog = new ChatLog(null, 1, ChatRoleEnum.ASSISTANT.getRole(), chatResult.getId(),  chatResult.getId(), chatResult.getChoices().get(0).getMessage().getContent(), new Date());
      chatLogMapper.insert(chatLog);

      return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "请求成功", chatResult);
    }catch (Exception e) {
      log.info(e.toString());
      return null;
    }

  }


  /**
   * 聊天（stream版）
   * @param signKey 聊天key
   * @return stream
   */
  public SseEmitter chatStream(String signKey) {

    // new SseEmitter timeout设置为0表示不超时
    SseEmitter emitter = new SseEmitter();

    // 构建请求参数
    Map<String, Object> data = buildRequestParams(signKey);
    // 构建请求头
    Map<String, String> headers = buildRequestHeaders(API_KEY);

    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.connectTimeout(Duration.ofSeconds(300));
    builder.readTimeout(Duration.ofSeconds(300));
    builder.writeTimeout(Duration.ofSeconds(300));
    OkHttpClient okHttpClient = builder.build();

    Request request = new Request.Builder()
            .url(chatUrl)
            .headers(Headers.of(headers))
            .post(RequestBody.create(JSON.toJSONString(data), MediaType.parse("application/json")))
            .build();

    // 新建线程发送 SSE 事件流数据
    asyncTaskExecutePool.chatAsyncTaskPool().execute(() -> {
      try (Response response = okHttpClient.newCall(request).execute()) {
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
                log.info("消息推送完毕，开始写入数据库");
                // 写入数据库
                ChatLog chatLog = new ChatLog(
                        null,
                        1,
                        ChatRoleEnum.ASSISTANT.getRole(),
                        messageId,
                        conversionId,
                        chatMessageBuffer.toString(),
                        new Date()
                );
                // 异步插入线程池
                asyncTask.setChatLog(chatLog);
                // 塞入缓存
                push(conversionId, JSON.toJSONString(chatLog));
                // 清空
                chatMessageBuffer.delete(0, chatMessageBuffer.length());
                messageId = null;

                emitter.complete();
                return;
              }
              // json转实体
              ChatStreamResult chatStreamResult = JSON.parseObject(line, ChatStreamResult.class);

              if ("assistant".equals(chatStreamResult.getChoices().get(0).getDelta().getRole())) {
                continue;
              }

              if ("stop".equals(chatStreamResult.getChoices().get(0).getFinishReason())) {
                emitter.complete();
                return;
              }


              /**
               * 获取消息id
               */
              if (StringUtils.isBlank(messageId)) {
                messageId = chatStreamResult.getId();
              }
              // 收集返回值
              String chatItem = chatStreamResult.getChoices().get(0).getDelta().getContent();
              if (null != chatItem) {
                chatMessageBuffer.append(chatItem);
                System.out.println(chatMessageBuffer);
              }
              // 构造返回体
              ChatStreamVO chatStream = new ChatStreamVO(messageId, conversionId, chatStreamResult.getChoices().get(0).getDelta().getContent());
              emitter.send(SseEmitter.event().data(chatStream));
            }
          }
        } else {
          emitter.completeWithError(new RuntimeException("Failed to read SSE data from server"));
        }
        emitter.complete(); // SSE 事件流数据发送完成后需要调用 complete() 方法
      } catch (IOException e) {
        log.info("请求chat流式接口发生异常, e: {}", e.toString());
        emitter.completeWithError(e); // 发送 SSE 事件流数据出现异常时需要调用 completeWithError() 方法
      }
    });

  // 返回 SseEmitter 实例
    return emitter;

  }


  /**
   * 生成请求体
   * @param key 缓存key
   * @return
   */
  private Map<String, Object> buildRequestParams(String key) {

    Map<String, Object> data = new HashMap<>();
    data.put("model", "gpt-3.5-turbo");
    data.put("stream", true);
    data.put("max_tokens", 1024);
    data.put("temperature", 0.5);

    ArrayList<Map<String, Object>> messageList = new ArrayList<>();

    // 从缓存中获取历史消息
    List<String> allChat = getAllElements(key);
    for (String chat : allChat) {
      ChatLog chatLog = JSON.parseObject(chat, ChatLog.class);
      conversionId = chatLog.getConversationId();
      Map<String, Object> message = new HashMap<>();
      message.put("role", chatLog.getRole());
      message.put("content", chatLog.getContent());
      messageList.add(message);
    }

    data.put("messages", messageList);

    log.info("请求参数为：{}", data);

    return data;
  }

  /**
   * 生成请求头
   * @param key chat秘钥
   * @return 请求头
   */
  private Map<String, String> buildRequestHeaders(String key) {

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Authorization", "Bearer " + key);
    headers.put("Accept", "text/event-stream");
    return headers;
  }

  /**
   * chat签名
   * @param process 请求实体
   * @return
   */
  public ResponseResult<String> chatSign(ChatProcess process) {

    // 校验敏感词
    boolean contains = SensitiveWordHelper.contains(process.getPrompt());
    if (contains) {
      ChatSensitiveLog sensitiveLog = new ChatSensitiveLog(null, 1, ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), ValueUtils.getUUID(), process.getPrompt(), new Date());
      asyncTask.setChatSensitiveLog(sensitiveLog);
      return new ResponseResult<>(ResultCode.NOT_PERMISSION.getCode(), "请勿输入非法词汇");
    }

    String signKey = "";

    // 加密key
    if (StringUtils.isNotBlank(process.getConversationId())) {
      signKey = process.getConversationId();
    }else {
     signKey = ValueUtils.getUUID();
    }

    // 写数据库
    ChatLog chatLog = new ChatLog(null, 1, ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), signKey, process.getPrompt(), new Date());
    asyncTask.setChatLog(chatLog);
    // 放入缓存队列
    push(signKey, JSON.toJSONString(chatLog));

    // 返回key
    log.info("请求chatSign结束, 返回值key: {}", signKey);
    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", signKey);
  }


  /**
   * 长度为三的队列
   * @param key key
   * @param element value
   */
  public void push(String key, String element) {
    redisTemplate.opsForList().rightPush(key, element);
    if (redisTemplate.opsForList().size(key) > 3) {
      redisTemplate.opsForList().leftPop(key);
    }
  }

  /**
   * 获取长度为3的队列
   * @param key key
   * @return 队列信息
   */
  public List<String> getAllElements(String key) {
    return redisTemplate.opsForList().range(key, 0, -1)
            .stream().map(Object::toString)
            .collect(Collectors.toList());
  }


}
