package com.aibot.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间格式化配置
 *
 * @date 2022-10-04 09:00:35
 */
@ControllerAdvice
public class TimeFormatConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      // Date类型返回及接收时间格式化
      builder.dateFormat(dateFormat);
      // 返回LocalDateTime时间类型序列化
      builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
      // 接收LocalDateTime时间类型反序列化，只对@RequestBody生效，对url中的参数不生效
      builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
    };
  }

  /**
   * 只对url中的参数生效,如http://localhost:8080/user/user/insert?createTime=2022-10-07 08:17:54，对@RequestBody不生效
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    // 接收Date时间类型格式化
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    // 接收LocalDateTime时间类型格式化
    binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        if (StringUtils.isNotBlank(text)) {
          setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
      }
    });
  }
}

