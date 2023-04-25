package com.aibot.service;

import com.aibot.beans.dto.OrderProductDTO;
import com.aibot.beans.entity.Product;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.enums.ResultCode;
import com.aibot.mapper.ProductMapper;
import com.aibot.pay.PayService;
import com.aibot.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;

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

  /** 商户号 */
  public static String merchantId = "1600419163";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "xxxx";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "123xxx";
  /** 商户APIV3密钥 */
  public static String apiV3key = "sxsFhwereewrq975839dfreewrq9WGTQ";

  /**
   * 购买商品
   * @param dto
   * @return
   */
  public ResponseResult<Object> orderProduct(OrderProductDTO dto) {

    // 验证商品是否存在
//    Product product = productMapper.selectById(dto.getProductId());
//    if (null == product) {
//      return new ResponseResult<>(ResultCode.PRODUCT_NOT_FOUND.getCode(), ResultCode.PRODUCT_NOT_FOUND.getMsg());
//    }
//    if (product.getCount() < dto.getCount() || product.getPutStatus() == 0) {
//      return new ResponseResult<>(ResultCode.PRODUCT_NOT_PERMIT.getCode(), ResultCode.PRODUCT_NOT_PERMIT.getMsg());
//    }

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

  public ResponseResult<Object> orderWx() {

    // 使用自动更新平台证书的RSA配置
    // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
    Config config =
            new RSAAutoCertificateConfig.Builder()
                    .merchantId(merchantId)
                    .privateKeyFromPath(privateKeyPath)
                    .merchantSerialNumber(merchantSerialNumber)
                    .apiV3Key(apiV3key)
                    .build();
    JsapiService service = new JsapiService.Builder().config(config).build();
    // request.setXxx(val)设置所需参数，具体参数可见Request定义
    PrepayRequest request = new PrepayRequest();
    Amount amount = new Amount();
    amount.setTotal(100);
    request.setAmount(amount);
    request.setAppid("wxa9d9651ae******");
    request.setMchid("190000****");
    request.setDescription("测试商品标题");
    request.setNotifyUrl("https://notify_url");
    request.setOutTradeNo("out_trade_no_001");
    Payer payer = new Payer();
    payer.setOpenid("oLTPCuN5a-nBD4rAL_fa********");
    request.setPayer(payer);
    PrepayResponse response = service.prepay(request);
    System.out.println(response.getPrepayId());

    return null;

  }
}
