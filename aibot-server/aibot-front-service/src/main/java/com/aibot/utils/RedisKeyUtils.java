package com.aibot.utils;

/**
 * redis的key工具
 *
 * @author: aabb
 * @create: 2023-03-20 12:32
 */
public class RedisKeyUtils {



  public static String getSmsVerifyKey(String type, String phone) {
    return "sms-".concat(type).concat("-").concat(phone);
  }


}
