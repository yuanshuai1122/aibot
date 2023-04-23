package com.aibot.controller;

import com.aibot.beans.dto.OrderProductDTO;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.service.OrderService;
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
 * @author: aabb
 * @create: 2023-03-23 17:51
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private OrderService orderService;


  /**
   * 创建订单
   *
   * @param dto DTO
   * @return {@link ResponseResult}<{@link Object}>
   */
  @PostMapping("/create")
  public ResponseResult<Object> orderProduct(@RequestBody @Valid OrderProductDTO dto) {
    if (dto.getProductId() <= 0 || dto.getCount() <= 0) {
      return new ResponseResult<>(ResultCode.PARAM_IS_INVAlID.getCode(), ResultCode.PARAM_IS_INVAlID.getMsg(), null);
    }

    return orderService.orderProduct(dto);
  }

}
