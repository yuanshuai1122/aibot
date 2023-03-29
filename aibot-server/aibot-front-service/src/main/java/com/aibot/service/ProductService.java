package com.aibot.service;

import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务
 *
 * @author: yuanshuai
 * @create: 2023-03-29 10:17
 */
@Service
@Slf4j
public class ProductService {

  @Autowired
  private ProductMapper productMapper;

  /**
   * 查询商品列表
   * @return
   */
  public ResponseResult<List<Product>> productList() {

    List<Product> products = productMapper.selectList(null);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", products);

  }
}
