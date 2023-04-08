package com.aibot.controller;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.dto.ImagesCreateDTO;
import com.aibot.beans.dto.chatProcess.ChatProcess;
import com.aibot.beans.vo.ImagesUrlCreateVO;
import com.aibot.service.ChatService;
import com.aibot.service.ImagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;

/**
 * chatgpt的api版控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-06 11:58
 */
@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {

  private final ChatService chatService;

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  //@PostMapping("/chat")
  //public ResponseResult<ChatResult> chatCommon(@RequestBody ChatCommonDTO dto) {
  //  log.info("开始请求chat：{}", dto);
  //  return chatApiService.chatCommon(dto);
  //}

  @PostMapping("/sign")
  public ResponseResult<String> chatSign(@RequestBody @Valid ChatProcess process) {
    log.info("开始请求chatsign：{}", process);
    return chatService.chatSign(process);
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter chatStream(@RequestParam("signKey") String signKey) {
    log.info("开始请求chat(Stream版), key: {}", signKey);
    return chatService.chatStream(signKey);
  }


}