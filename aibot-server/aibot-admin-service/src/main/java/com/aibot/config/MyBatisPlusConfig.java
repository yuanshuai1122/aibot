package com.aibot.config;

import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.utils.EntityUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * mp分页插件
 *
 * @author: yuanshuai
 * @create: 2023-03-28 17:29
 */
@Configuration
public class MyBatisPlusConfig {

  @Autowired
  private TenantIdManager tenantIdManager;

  @Autowired
  private HttpServletRequest request;

  @Bean
  public MybatisPlusInterceptor paginationInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    // 多租户插件
    TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
    tenantInterceptor.setTenantLineHandler(new TenantLineHandler() {
      @Override
      public Expression getTenantId() {
        // 返回当前用户的租户ID
        return new LongValue(tenantIdManager.getCurrentTenantId());
      }

      @Override
      public String getTenantIdColumn() {
        return TenantLineHandler.super.getTenantIdColumn();
      }

      @Override
      public boolean ignoreTable(String tableName) {
        /**
         * 此处的list，临时用作拦截
         * 原因是：下面的表解析方法，用的是大驼峰转下划线，再跟sql拦截器拦截到的表名对比，如果匹配到了，则认为该表需要多租户拼接
         * 但是有的表没有严格的按照大驼峰转下划线，所以这些表需要额外定义
         * @TableName 这个注解能否完成该职责，目前还未测试，以后再说。
         */
        List<String> list = new ArrayList<>();
        // 超管不做拦截
        if (!UserRoleEnum.SUPER_ADMIN.getRole().equals(request.getAttribute("role").toString())) {
          list.add("user");
          list.add("product");
          list.add("user_orders");
          list.add("distribution_level_config");
          list.add("distribution_config");
        }

        return !list.contains(tableName);

      }

      @Override
      public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
      }
    });
    // 租户插件
    interceptor.addInnerInterceptor(tenantInterceptor);
    // 分页插件
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }



}
