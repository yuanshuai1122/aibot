package com.aibot.utils;

import com.aibot.beans.dto.entity.ChatSuccessLog;
import com.aibot.beans.dto.entity.chatProcess.ChatProcess;
import com.aibot.beans.dto.entity.chatProcess.ChatPrompt;
import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求工具类
 *
 * @author: aabb
 * @create: 2023-03-23 22:59
 */
@Slf4j
public class RequestUtils {

  /**
   * 生成请求头
   * @param key chat秘钥
   * @return 请求头
   */
  public static Map<String, String> buildRequestHeaders(String key) {

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Authorization", "Bearer " + key);
    headers.put("Accept", "text/event-stream");
    return headers;
  }


  /**
   * 构建请求参数
   *
   * @param value 价值
   * @return {@link Map}<{@link String}, {@link Object}>
   */
  public static Map<String, Object> buildRequestParams(String value) {
    Map<String, Object> data = new HashMap<>();
    data.put("model", "gpt-3.5-turbo");
    data.put("stream", true);
    //data.put("max_tokens", 1024);
    //data.put("temperature", 0.5);
    return data;
  }

}
