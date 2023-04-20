package com.aibot.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具类
 *
 * @author: aabb
 * @create: 2023-03-23 22:59
 */
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
   * 生成请求体
   * @param key 缓存key
   * @return
   */
  public static Map<String, Object> buildRequestParams(String key) {

    Map<String, Object> data = new HashMap<>();
    data.put("model", "gpt-3.5-turbo");
    data.put("stream", true);
    data.put("max_tokens", 256);
    //data.put("temperature", 0.5);

    return data;
  }

}
