package com.aibot.controller;

import com.aibot.beans.dto.CreateProductDTO;
import com.aibot.beans.dto.UpdateProductDTO;
import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 商品控制器
 *
 * @author: yuanshuai
 * @create: 2023-03-29 11:53
 */
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;


  @GetMapping("/list")
  public ResponseResult<HashMap<String, Object>> productList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
    return productService.productList(pageNum, pageSize);
  }

  @GetMapping("/info")
  public ResponseResult<Product> productInfo(@RequestParam("id") Integer id) {
    return productService.productInfo(id);
  }


  @PostMapping("/update")
  public ResponseResult<Object> productUpdate(@RequestBody @Valid UpdateProductDTO dto) {
    if (dto.getId() == null) {
      return new ResponseResult<>(ResultCode.PRODUCT_NOT_FOUND.getCode(), ResultCode.PRODUCT_NOT_FOUND.getMsg());
    }
    return productService.productUpdate(dto);
  }

  @PostMapping("/create")
  public ResponseResult<Object> productCreate(@RequestBody @Valid CreateProductDTO dto) {
    if (dto.getProductPrice() == null || dto.getCount() == null || dto.getPutStatus() == null) {
      return new ResponseResult<>(ResultCode.PRODUCT_CREATE_FAILURE.getCode(), ResultCode.PRODUCT_CREATE_FAILURE.getMsg());
    }

    return productService.productCreate(dto);
  }






}
