package com.chatgpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池
 *
 * @author: yuanshuai
 * @create: 2023-03-15 18:23
 */
@EnableAsync
@Configuration
public class AsyncTaskExecutePool {

  //核心线程池大小
  private final int corePoolSize = 1000;
  //最大线程数
  private final int maxPoolSize = 1500;
  //队列容量
  private final int queueCapacity = 5000;
  //活跃时间/秒
  private final int keepAliveSeconds = 1200;

  @Bean
  public Executor chatAsyncTaskPool() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //核心线程池大小
    executor.setCorePoolSize(corePoolSize);
    //最大线程数
    executor.setMaxPoolSize(maxPoolSize);
    //队列容量
    executor.setQueueCapacity(queueCapacity);
    //活跃时间
    executor.setKeepAliveSeconds(keepAliveSeconds);
    //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    executor.setWaitForTasksToCompleteOnShutdown(true);
    //线程名字前缀
    executor.setThreadNamePrefix("my-async1--");
    // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
    // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return executor;
  }
}
