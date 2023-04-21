package com.aibot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author: yuanshuai
 * @create: 2023-02-22 18:17
 */
@Configuration
@EnableAsync
public class LogExecutorConfig {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  /** 核心线程数 */
  private int corePoolSize = 0;
  /** 最大线程数 */
  private int maxPoolSize = 1;
  /** 队列数 */
  private int queueCapacity = Integer.MAX_VALUE;

  @Bean(name = "logAsyncTaskPool")
  public Executor threadPoolTaskExecutor(){
    logger.info("创建日志线程池...");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //设置核心线程数
    executor.setCorePoolSize(corePoolSize);
    //设置最大线程数
    executor.setMaxPoolSize(maxPoolSize);
    //设置空闲时间
    executor.setKeepAliveSeconds(60);
    //设置队列数
    executor.setQueueCapacity(queueCapacity);
    //设置线程名称前缀
    executor.setThreadNamePrefix("log-thread->>>");

    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
    // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
    // 设置拒绝策略.当工作队列已满,线程数为最大线程数的时候,接收新任务抛出RejectedExecutionException异常
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    // 执行初始化
    executor.initialize();
    return executor;
  }

}
