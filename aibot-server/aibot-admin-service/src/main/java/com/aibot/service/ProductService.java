package com.aibot.service;

import com.aibot.beans.dto.CreateProductDTO;
import com.aibot.beans.dto.UpdateProductDTO;
import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.User;
import com.aibot.beans.entity.UserInfo;
import com.aibot.beans.vo.ProductVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.constants.enums.UserRoleEnum;
import com.aibot.mapper.ProductMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

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

  @Autowired
  private HttpServletRequest request;

  /**
   * 查询已上架商品列表
   * @return
   */
  public ResponseResult<HashMap<String, Object>> productList(String productName, Integer pageNum, Integer pageSize) {

    MPJLambdaWrapper<Product> wrapper = new MPJLambdaWrapper<Product>()
            .select(Product::getId, Product::getProductName, Product::getProductPrice, Product::getImgUrl, Product::getCount, Product::getPutStatus, Product::getProductDescription, Product::getCreateTime, Product::getUpdateTime)
            .innerJoin(UserInfo.class, UserInfo::getUserId, Product::getTenantId)
            .selectAs(UserInfo::getNickName, ProductVO::getTenantName)
            .eq(Product::getPutStatus, 1)
            .like(StringUtils.isNotBlank(productName), Product::getProductName, productName)
            ;

    Page<ProductVO> productPage = productMapper.selectJoinPage(new Page<>(pageNum, pageSize), ProductVO.class, wrapper);
    HashMap<String, Object> map = new HashMap<>();
    map.put("productList", productPage.getRecords());
    map.put("totalCount", productPage.getTotal());

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", map);

  }

  public ResponseResult<Product> productInfo(Integer id) {

    String role = request.getAttribute("role").toString();
    if (StringUtils.isBlank(role) || !UserRoleEnum.SUPER_ADMIN.getRole().equals(role)) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "用户权限不足", null);
    }

    Product product = productMapper.selectById(id);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", product);

  }


  public ResponseResult<Object> productUpdate(UpdateProductDTO dto) {

    String role = request.getAttribute("role").toString();
    if (StringUtils.isBlank(role) || !UserRoleEnum.SUPER_ADMIN.getRole().equals(role)) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "用户权限不足", null);
    }

    Product product = new Product(dto.getId(),null,  dto.getProductName(), dto.getProductPrice(), dto.getImgUrl(), dto.getCount(), dto.getPutStatus(), dto.getProductDescription(), null, new Date());
    int flag = productMapper.updateById(product);
    if (flag <= 0) {
      return new ResponseResult<>(ResultCode.PRODUCT_UPDATE_FAILURE.getCode(), ResultCode.PRODUCT_UPDATE_FAILURE.getMsg());
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "更新成功", null);
  }

  public ResponseResult<Object> productCreate(CreateProductDTO dto) {

    String role = request.getAttribute("role").toString();
    if (StringUtils.isBlank(role) || !UserRoleEnum.SUPER_ADMIN.getRole().equals(role)) {
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "用户权限不足", null);
    }

    String tenantId = request.getAttribute("tenantId").toString();

    Product product = new Product(null, Integer.parseInt(tenantId),dto.getProductName(), dto.getProductPrice(), dto.getImgUrl(), dto.getCount(), dto.getPutStatus(), dto.getProductDescription(), new Date(), new Date());
    int flag = productMapper.insert(product);
    if (flag <= 0) {
      return new ResponseResult<>(ResultCode.PRODUCT_CREATE_FAILURE.getCode(), ResultCode.PRODUCT_CREATE_FAILURE.getMsg());
    }

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "新增成功", null);
  }
}
