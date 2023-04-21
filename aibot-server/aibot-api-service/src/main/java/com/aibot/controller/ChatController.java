package com.aibot.controller;

import com.aibot.beans.dto.entity.ChatResult.ChatResult;
import com.aibot.beans.dto.entity.ResponseResult;
import com.aibot.beans.dto.entity.chatProcess.ChatProcess;
import com.aibot.config.sign.Signature;
import com.aibot.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;

/**
 * chatgpt的api版控制器
 *
 * @author: aabb
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

  /**
   * 聊天（普通版）
   *
   * @param dto DTO
   * @return {@link ResponseResult}<{@link ChatResult}>
   */
  @Signature
  @PostMapping("/sync")
  public ResponseResult<ChatResult> chatCommon(@RequestBody ChatProcess dto) {
    log.info("开始请求chat：{}", dto);
    return chatService.chatCommon(dto);
  }


  /**
   * 聊天（流式）
   *
   * @param signKey 签名密钥
   * @return {@link SseEmitter}
   */
  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter chatStream(@RequestParam("signKey") String signKey) {
    log.info("开始请求chat(Stream版), key: {}", signKey);
    return chatService.chatStream(signKey);
  }


}
