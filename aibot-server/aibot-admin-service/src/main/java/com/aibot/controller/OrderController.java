package com.aibot.controller;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 订单控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-30 12:05
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {


  @Autowired
  private OrderService orderService;

  @GetMapping("/list")
  public ResponseResult<HashMap<String, Object>> productList(@RequestParam(value = "nickName", required = false) String nickName,
                                                             @RequestParam(value = "productName", required = false) String productName,
                                                             @RequestParam(value = "orderStatus", required = false) String orderStatus,
                                                             @RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize) {
    return orderService.productList(nickName, productName, orderStatus, pageNum, pageSize);
  }

}
