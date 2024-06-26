package com.aibot.service;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.SmsSendDTO;
import com.aibot.enums.ResultCode;
import com.aibot.enums.SmsTypeEnum;
import com.aibot.utils.MtUtils;
import com.aibot.utils.RedisKeyUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 短信服务
 *
 * @author: aabb
 * @create: 2023-03-27 11:49
 */
@Service
@Slf4j
public class SmsService {

  private final String mtAppId = "ywcs";

  private final String mtSecretKey = "VT9xdDkGaHc3ZQyn";

  @Autowired
  private RedisTemplate<String, String> redisTemplate;


  /**
   * 根据手机号发短信
   * @param dto 发短信实体
   * @return 发短信结果
   */
  public ResponseResult<Object> smsSend(SmsSendDTO dto) {

    // 获取短信类型
    SmsTypeEnum typeEnum = SmsTypeEnum.search(dto.getType());
    if (null == typeEnum) {
      log.info("发送短信失败：非法短信类型, phone:{}", dto.getPhone());
      return new ResponseResult<>(ResultCode.FAILED.getCode(), "非法短信类型");
    }
    // 获取模板code
    String code = typeEnum.getCode();

    Map params = new HashMap<>();
    // 填充参数
    params.put("phoneNumber", dto.getPhone());
    params.put("smsSignId", code);

    // 这里替换成您的 appId 和 secretKey
    String appId = mtAppId;
    String secretKey = mtSecretKey;

    // 计算签名
    String signStr = MtUtils.sign(appId, secretKey, new Date(), 30, params);

    // 加密请求内容
    byte[] data = new JSONObject(params).toString().getBytes(StandardCharsets.UTF_8);
    String encryptReqData = MtUtils.encrypt(data, secretKey);
    // 准备请求
    String url = "https://open.miitang.com/v1/tools/sms/code/sender";

    Map headers = new HashMap<>();
    headers.put("X-Mce-Signature",signStr);
    headers.put("Content-Type", "application/json;charset=utf-8");

    try {
      /**
       * 重要提示如下:
       * MtUtils 请从
       * https://static.miitang.com/saas/simple/MtUtils.java 下载
       *
       * 相应的依赖请参照
       * https://static.miitang.com/saas/simple/pom.xml
       */
      // 发送请求
      HttpResponse response = MtUtils.doPost(url, headers, encryptReqData);
      // 获取 response 的 body
      String resStr = EntityUtils.toString(response.getEntity());
      if(resStr!=null && !resStr.contains("{")) {
        resStr = MtUtils.decrypt(resStr, secretKey);
      }
      System.out.println(resStr);
      int statusCode = response.getStatusLine().getStatusCode();
      if(statusCode == 200){
        // 请求成功，可根据业务码（请求体中的code）进行逻辑处理
        JSONObject jsonObject = JSON.parseObject(resStr);
        String verifyCode = jsonObject.get("verificationCode").toString();
        // 存redis
        redisTemplate.opsForValue().set(RedisKeyUtils.getSmsVerifyKey(typeEnum.getType(), dto.getPhone()), verifyCode, 5, TimeUnit.MINUTES);
        log.info("用户：{}, 发送短信成功, code: {}", dto.getPhone(), "200");
        return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "短信发送成功", null);
      } else if(statusCode == 610){
        // 用户输入的参数问题，可直接提示用户
        log.info("用户：{}, 发送短信失败, code: {}", dto.getPhone(), "610");
        return new ResponseResult<>(ResultCode.SMS_PARAMS_ERROR.getCode(), ResultCode.SMS_PARAMS_ERROR.getMsg(), null);
      } else if(statusCode == 611){
        // 系统准备的数据问题，如 文件数据下载失败、数据不存在、数据重复请求等。  LOG it and 提示用户
        log.info("用户：{}, 发送短信失败, code: {}", dto.getPhone(), "611");
        return new ResponseResult<>(ResultCode.SMS_DATA_ERROR.getCode(), ResultCode.SMS_DATA_ERROR.getMsg(), null);
      } else if(statusCode == 612){
        // 用户操作频度问题，可提示用户。  LOG it and 按业务特点做处理
        log.info("用户：{}, 发送短信失败, code: {}", dto.getPhone(), "612");
        return new ResponseResult<>(ResultCode.SMS_TIMES_ERROR.getCode(), ResultCode.SMS_TIMES_ERROR.getMsg(), null);
      } else if(statusCode >= 500 && statusCode < 600){
        // 在运行阶段发生的系统稳定性问题，客户端可以重试，或者联系我司客服
        log.info("用户：{}, 发送短信失败, code: {}", dto.getPhone(), "500-600");
        return new ResponseResult<>(ResultCode.SMS_SYSTEM_ERROR.getCode(), ResultCode.SMS_SYSTEM_ERROR.getMsg(), null);
      } else {
        // 如账户密码错误、IP白名单问题、余额不足等，您应该在对接过程中解决相关问题。
        log.info("用户：{}, 发送短信失败, code: {}", dto.getPhone(), "余额不足");
        return new ResponseResult<>(ResultCode.SMS_FALIURE_ERROR.getCode(), ResultCode.SMS_FALIURE_ERROR.getMsg(), null);
      }
    } catch (Exception e) {
      log.info("发送短信出现异常, {}", e.toString());
      return new ResponseResult<>(ResultCode.SMS_EXCEPTION_ERROR.getCode(), ResultCode.SMS_EXCEPTION_ERROR.getMsg(), null);
    }

  }
}
