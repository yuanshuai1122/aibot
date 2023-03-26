package com.aibot.utils;

/**
 * redis的key工具
 *
 * @author: yuanshuai
 * @create: 2023-03-20 12:32
 */
public class RedisKeyUtils {


  public static String getFlushKey(String ip) {
    return "flush-".concat(ip);
  }

  public static String getUserKey(String chatKey) {
    return "user-".concat(chatKey);
  }

  public static String getChatKey(String chatKey) {
    return "chatkey-".concat(chatKey);
  }

  public static String getLoginKey(String account) {
    return "Login-".concat(account);
  }

}
