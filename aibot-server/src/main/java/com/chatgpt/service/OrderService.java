package com.chatgpt.service;

import com.chatgpt.beans.entity.Product;
import com.chatgpt.beans.entity.ResponseResult;
import com.chatgpt.beans.dto.OrderProductDTO;
import com.chatgpt.beans.entity.UserOrders;
import com.chatgpt.constants.enums.ResultCode;
import com.chatgpt.mapper.ProductMapper;
import com.chatgpt.mapper.UserOrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 支付服务
 *
 * @author: yuanshuai
 * @create: 2023-03-23 18:01
 */
@Slf4j
@Service
public class OrderService {

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private UserOrdersMapper userOrdersMapper;

  /**
   * 购买商品
   * @param dto
   * @return
   */
  public ResponseResult<Object> orderProduct(OrderProductDTO dto) {

    // 验证商品是否存在
    Product product = productMapper.selectById(dto.getProductId());
    if (null == product) {
      return new ResponseResult<>(ResultCode.PRODUCT_NOT_FOUND.getCode(), ResultCode.PRODUCT_NOT_FOUND.getMsg());
    }

    // 创建订单
    UserOrders orders = new UserOrders(
            null,
            Integer.parseInt(request.getAttribute("id").toString()),
            product.getId(),
            dto.getCount(), product.getProductPrice().multiply(new BigDecimal(dto.getCount())),

    );


    return null;


  }
}
