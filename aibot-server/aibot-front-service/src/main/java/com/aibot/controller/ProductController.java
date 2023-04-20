package com.aibot.controller;

import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.ProductVO;
import com.aibot.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品控制器
 *
 * @author: aabb
 * @create: 2023-03-29 10:16
 */
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;


  @GetMapping("/list")
  public ResponseResult<List<ProductVO>> productList() {
    return productService.productList();
  }

}
