package com.aibot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.aibot.filter")
public class AiBotFrontServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AiBotFrontServiceApplication.class, args);
  }
}
