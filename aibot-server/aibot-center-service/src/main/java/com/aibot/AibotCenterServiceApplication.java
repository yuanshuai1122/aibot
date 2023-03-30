package com.aibot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.aibot.filter")
public class AibotCenterServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AibotCenterServiceApplication.class, args);
  }

}
