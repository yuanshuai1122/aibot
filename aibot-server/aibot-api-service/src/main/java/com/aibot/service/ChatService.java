package com.aibot.service;

import com.aibot.beans.dto.entity.ChatApiKey;
import com.aibot.beans.dto.entity.ChatFailureLog;
import com.aibot.beans.dto.entity.ChatStreamResult.ChatStreamResult;
import com.aibot.beans.dto.vo.ChatStreamVO;
import com.aibot.config.AsyncTaskExecutePool;
import com.aibot.config.OkHttpClientSingleton;
import com.aibot.constants.ApiBaseUrl;
import com.aibot.mapper.ChatApiKeyMapper;
import com.aibot.mapper.ChatSuccessLogMapper;
import com.aibot.tasks.AsyncTask;
import com.aibot.utils.RequestUtils;
import com.alibaba.fastjson2.JSON;
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
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ChatSuccessLogMapper chatSuccessLogMapper;

  @Autowired
  private HttpServletRequest request;



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

    log.info("构建请求体, data: {}", data);

    // 静态okhttpClient
    OkHttpClient.Builder builder = OkHttpClientSingleton.getInstance().newBuilder();
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

              // json转实体
              ChatStreamResult chatStreamResult = JSON.parseObject(line, ChatStreamResult.class);

              if ("assistant".equals(chatStreamResult.getChoices().get(0).getDelta().getRole())) {
                continue;
              }

              // 收集返回值
              String chatItem = chatStreamResult.getChoices().get(0).getDelta().getContent();
              // 构造返回体
              ChatStreamVO chatStream = new ChatStreamVO(messageId, conversionId, chatItem);

              emitter.send(SseEmitter.event().data(chatStream));
            }
          }
        } else {
          ChatFailureLog chatFailureLog = new ChatFailureLog(null, 99, messageId, conversionId, "CHAT响应失败或响应为空", new Date());
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


}
