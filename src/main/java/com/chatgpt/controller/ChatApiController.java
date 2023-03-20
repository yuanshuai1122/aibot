package com.chatgpt.controller;

import com.chatgpt.beans.ChatLog;
import com.chatgpt.beans.ChatResult.ChatResult;
import com.chatgpt.beans.ResponseResult;
import com.chatgpt.beans.dto.ChatCommonDTO;
import com.chatgpt.beans.dto.chatProcess.ChatProcess;
import com.chatgpt.service.ChatApiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.util.List;

/**
 * chatgpt的api版控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-06 11:58
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class ChatApiController {

  private final ChatApiService chatApiService;

  public ChatApiController(ChatApiService chatApiService) {
    this.chatApiService = chatApiService;
  }

  @PostMapping("/chat")
  public ResponseResult<ChatResult> chatCommon(@RequestBody ChatCommonDTO dto) {
    log.info("开始请求chat：{}", dto);
    return chatApiService.chatCommon(dto);
  }

  @PostMapping("/chat/sign")
  public ResponseResult<String> chatSign(@RequestBody @Valid ChatProcess process) {
    log.info("开始请求chatsign：{}", process);
    return chatApiService.chatSign(process);
  }

  @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter chatStream(@RequestParam("signKey") String signKey) {
    log.info("开始请求chat(Stream版), key: {}", signKey);
    return chatApiService.chatStream(signKey);
  }


}
