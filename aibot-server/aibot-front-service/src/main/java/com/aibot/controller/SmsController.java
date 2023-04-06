package com.aibot.controller;

import com.aibot.annotation.AccessLimit;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.SmsSendDTO;
import com.aibot.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 短信控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-27 11:46
 */
@RestController
@Slf4j
@RequestMapping("/sms")
public class SmsController {

  @Autowired
  private SmsService smsService;


  @PostMapping("/send")
  //    表示此接口 3 秒内最大访问次数为 2，禁用时长为 40 秒
  //@AccessLimit(second = 3, maxTime = 2, forbiddenTime = 40L)
  public ResponseResult<Object> smsSend(@RequestBody @Valid SmsSendDTO dto) {

    return smsService.smsSend(dto);
  }

}
