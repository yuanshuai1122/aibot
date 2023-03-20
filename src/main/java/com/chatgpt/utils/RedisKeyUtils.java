package com.chatgpt.utils;

/**
 * redis的key工具
 *
 * @author: yuanshuai
 * @create: 2023-03-20 12:32
 */
public class RedisKeyUtils {


  public static String getLoginKey(String account) {
    return "Login-".concat(account);
  }

}
