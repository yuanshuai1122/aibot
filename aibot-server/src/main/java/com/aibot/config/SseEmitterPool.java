package com.aibot.config;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * SseEmitter对象池
 *
 * @author: yuanshuai
 * @create: 2023-03-23 22:29
 */
public class SseEmitterPool {
  private static final ConcurrentHashMap<Long, SseEmitter> SSES = new ConcurrentHashMap<>();

  public static SseEmitter borrow() {
    long threadId = Thread.currentThread().getId();
    SseEmitter emitter = SSES.get(threadId);
    if (emitter == null) {
      emitter = new SseEmitter();
      SSES.put(threadId, emitter);
    }
    return emitter;
  }

  public static void release() {
    long threadId = Thread.currentThread().getId();
    SSES.remove(threadId);
  }
}
