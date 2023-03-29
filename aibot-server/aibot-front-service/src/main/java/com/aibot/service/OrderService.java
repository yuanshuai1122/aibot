package com.aibot.service;

import com.aibot.beans.dto.OrderProductDTO;
import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
    if (product.getCount() < dto.getCount() || product.getPutStatus() == 0) {
      return new ResponseResult<>(ResultCode.PRODUCT_NOT_PERMIT.getCode(), ResultCode.PRODUCT_NOT_PERMIT.getMsg());
    }

    // 创建订单
    //UserOrders orders = new UserOrders(
    //        null,
    //        Integer.parseInt(request.getAttribute("id").toString()),
    //        product.getId(),
    //        dto.getCount(), product.getProductPrice().multiply(new BigDecimal(dto.getCount())),
    //
    //);


    return null;


  }
}
