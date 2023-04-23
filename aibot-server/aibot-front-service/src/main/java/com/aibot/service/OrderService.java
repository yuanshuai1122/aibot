package com.aibot.service;

import com.aibot.beans.dto.OrderProductDTO;
import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ProductMapper;
import com.aibot.pay.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 支付服务
 *
 * @author: aabb
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
  private PayService payService;

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

    // 支付
    try {
      payService.pay(dto.getChannel(), new BigDecimal(1));
    }catch (Exception e) {
      log.info("支付发生异常, e:{}", e);
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "支付失败，请稍后再试", null);
    }




    return null;


  }
}
