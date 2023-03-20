package com.chatgpt.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 数值相关工具类
 *
 * @author: yuanshuai
 * @create: 2023-03-14 23:17
 */
public class ValueUtils {

  private static final Random random = new Random();

  /**
   * 获取uuid
   * @return uuid
   */
  public static String getUUID() {
    return UUID.randomUUID().toString();
  }

  /**
   * 获取消息唯一id
   * @return 对话唯一id
   */
  public static String getMessageUUID() {
    return "usercmpl-".concat(UUID.randomUUID().toString());
  }

  public static double getRandomDouble() {
    return random.nextDouble();
  }

}
