package com.aibot.service;

import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.ProductVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ProductMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
   * 查询已上架商品列表
   * @return
   */
  public ResponseResult<List<ProductVO>> productList() {

    MPJLambdaWrapper<Product> wrapper = new MPJLambdaWrapper<Product>()
            .select(Product::getId, Product::getProductName, Product::getProductPrice, Product::getImgUrl, Product::getCount, Product::getProductDescription)
            .eq(Product::getPutStatus, 1);

    List<ProductVO> products = productMapper.selectJoinList(ProductVO.class, wrapper);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", products);

  }
}
