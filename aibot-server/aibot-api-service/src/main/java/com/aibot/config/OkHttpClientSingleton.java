package com.aibot.config;

import okhttp3.OkHttpClient;

/**
 * okhttp客户端
 *
 * @author: aabb
 * @create: 2023-03-23 22:24
 */
public class OkHttpClientSingleton {

  private static OkHttpClient instance;

  private OkHttpClientSingleton() {}

  public static OkHttpClient getInstance() {
    if (instance == null) {
      synchronized (OkHttpClientSingleton.class) {
        if (instance == null) {
          instance = new OkHttpClient();
        }
      }
    }
    return instance;
  }
}
