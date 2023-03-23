package com.chatgpt.controller;

import com.chatgpt.beans.entity.ResponseResult;
import com.chatgpt.beans.dto.OrderProductDTO;
import com.chatgpt.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 充值控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-23 17:51
 */
@RestController
@Slf4j
@RequestMapping("/pay")
public class PayController {

  @Autowired
  private PayService payService;


  @PostMapping("/product")
  public ResponseResult<Object> orderProduct(@RequestBody @Valid OrderProductDTO dto) {

    return payService.orderProduct(dto);

  }

}
