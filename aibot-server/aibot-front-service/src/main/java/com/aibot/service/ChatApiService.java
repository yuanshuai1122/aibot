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
   * 聊天（stream版）
   * @param signKey 聊天key
   * @return stream
   */
  public SseEmitter chatStream(String signKey) {

    // new SseEmitter timeout设置为0表示不超时
    SseEmitter emitter = new SseEmitter();
    log.info("创建SseEmitter, {}", emitter);

    // 获取随机一条key
    ChatApiKey apiKey = chatApiKeyMapper.selectRandomKey();

    log.info("开始构造流式请求，id:{}, apiKey:{}", apiKey.getId(), apiKey.getApiKey());

    // 构建请求头
    Map<String, String> headers = RequestUtils.buildRequestHeaders(apiKey.getApiKey());
    log.info("构建请求头, headers: {}", headers);

    // 构建请求体
    Map<String, Object> data = RequestUtils.buildRequestParams(signKey);

    ArrayList<Map<String, Object>> messageList = new ArrayList<>();
    // 从缓存中获取历史消息
    List<String> allChat = getAllElements(signKey);
    for (String chat : allChat) {
      ChatSuccessLog chatSuccessLog = JSON.parseObject(chat, ChatSuccessLog.class);
      conversionId = chatSuccessLog.getConversationId();
      Map<String, Object> message = new HashMap<>();
      message.put("role", chatSuccessLog.getRole());
      message.put("content", chatSuccessLog.getContent());
      messageList.add(message);
    }
    data.put("messages", messageList);

    log.info("构建请求体, data: {}", data);

    // 静态okhttpClient
    OkHttpClient.Builder builder = OkHttpClientSingleton.getInstance().newBuilder();
    log.info("构建okHttp: {}", builder);
    builder.connectTimeout(Duration.ofSeconds(300));
    builder.readTimeout(Duration.ofSeconds(300));
    builder.writeTimeout(Duration.ofSeconds(300));
    OkHttpClient okHttpClient = builder.build();

    Request req = new Request.Builder()
            .url(ApiBaseUrl.BASE_CHAT_URL)
            .headers(Headers.of(headers))
            .post(RequestBody.create(JSON.toJSONString(data), MediaType.parse("application/json")))
            .build();
    log.info("构建请求request: {}", req);

    // 新建线程发送 SSE 事件流数据
    // 设置子线程共享
    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    RequestContextHolder.setRequestAttributes(sra, true);
    asyncTaskExecutePool.chatAsyncTaskPool().execute(() -> {
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
                log.info("消息推送完毕，开始写入数据库");
                // 写入数据库
                ChatSuccessLog chatSuccessLog = new ChatSuccessLog(
                        null,
                        Integer.parseInt(request.getAttribute("id").toString()),
                        ChatRoleEnum.ASSISTANT.getRole(),
                        messageId,
                        conversionId,
                        chatMessageBuffer.toString(),
                        new Date()
                );
                // 异步插入线程池
                asyncTask.setChatLog(chatSuccessLog);
                // 塞入缓存
                push(conversionId, JSON.toJSONString(chatSuccessLog));
                // 清空
                chatMessageBuffer.delete(0, chatMessageBuffer.length());
                messageId = null;

                return;
              }
              // json转实体
              ChatStreamResult chatStreamResult = JSON.parseObject(line, ChatStreamResult.class);

              if ("assistant".equals(chatStreamResult.getChoices().get(0).getDelta().getRole())) {
                continue;
              }

              //if ("stop".equals(chatStreamResult.getChoices().get(0).getFinishReason())) {
              //  emitter.complete();
              //  return;
              //}

              // 赋值消息id
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
          ChatFailureLog chatFailureLog = new ChatFailureLog(null, Integer.parseInt(request.getAttribute("id").toString()), messageId, conversionId, "CHAT响应失败或响应为空", new Date());
          asyncTask.setChatLogFailure(chatFailureLog);
          emitter.completeWithError(new RuntimeException("CHAT响应失败或响应为空"));
        }

      } catch (IOException e) {
        log.info("请求chat流式接口发生异常, e: {}", e.toString());
        // 发送 SSE 事件流数据出现异常时需要调用 completeWithError() 方法
        emitter.completeWithError(new RuntimeException("请求服务器发生异常，请重试"));
        ChatFailureLog chatFailureLog = new ChatFailureLog(null, Integer.parseInt(request.getAttribute("id").toString()), messageId, conversionId, "请求服务器发生异常，请重试", new Date());
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

    String signKey = "";

    // 加密key
    if (StringUtils.isNotBlank(process.getConversationId())) {
      signKey = process.getConversationId();
    }else {
     signKey = ValueUtils.getUUID();
    }

    // 写日志数据库
    ChatSuccessLog chatSuccessLog = new ChatSuccessLog(null, Integer.parseInt(request.getAttribute("id").toString()), ChatRoleEnum.USER.getRole(), ValueUtils.getMessageUUID(), signKey, process.getPrompt(), new Date());
    asyncTask.setChatLog(chatSuccessLog);
    // 放入缓存队列
    push(signKey, JSON.toJSONString(chatSuccessLog));

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
