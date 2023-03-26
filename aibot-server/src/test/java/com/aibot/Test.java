package com.aibot;

import com.aibot.utils.ValueUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ss
 *
 * @author: yuanshuai
 * @create: 2023-03-21 21:38
 */
public class Test {

  @Autowired

  public static void main(String[] args) {
    System.out.println(ValueUtils.getApiKey());
  }
}
