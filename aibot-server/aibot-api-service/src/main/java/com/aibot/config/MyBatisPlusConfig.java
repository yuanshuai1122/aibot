package com.aibot.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mp分页插件
 *
 * @author: aabb
 * @create: 2023-03-28 17:29
 */
@Configuration
public class MyBatisPlusConfig {

  @Bean
  public MybatisPlusInterceptor paginationInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    // 分页插件
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }



}
