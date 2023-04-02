package com.aibot.service;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.CheckBillingVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.utils.DateUtils;
import com.aibot.utils.OkHttpUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统服务
 *
 * @author: yuanshuai
 * @create: 2023-04-02 14:02
 */
@Service
@Slf4j
public class SystemService {

  // 查是否订阅
  private static final String URL_SUBSCRIPTION = "https://api.openai.com/v1/dashboard/billing/subscription";
  // 查普通账单
  private static final String URL_BALANCE = "https://api.openai.com/dashboard/billing/credit_grants";
  // 查使用量
  private static final String URL_USAGE = "https://api.openai.com/v1/dashboard/billing/usage";
  // 格式化
  private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

  public ResponseResult<CheckBillingVO> checkBilling(String apiKey) {

    OkHttpUtils client = OkHttpUtils.builder();
    client.url(URL_SUBSCRIPTION);
    client.addHeader("Authorization", "Bearer " + apiKey);
    client.addHeader("Content-Type", "application/json");
    String sync = client.get().sync();
    System.out.println(sync);
    // 总额度
    BigDecimal totalAmount = new BigDecimal(JSON.parseObject(sync).get("hard_limit_usd").toString());

    OkHttpUtils client1 = OkHttpUtils.builder();
    client1.url(URL_USAGE);
    client1.addHeader("Authorization", "Bearer " + apiKey);
    client1.addHeader("Content-Type", "application/json");
    client1.addParam("start_date", fmt.format(DateUtils.getStartOfYear()));
    client1.addParam("end_date", fmt.format(DateUtils.getTimesnight()));
    String sync1 = client1.get().sync();
    System.out.println(sync1);
    // 已使用额度
    BigDecimal totalUsage = new BigDecimal(JSON.parseObject(sync1).get("total_usage").toString()).divide(new BigDecimal("100"));

    // 剩余额度
    BigDecimal remainAmount = totalAmount.subtract(totalUsage);

    CheckBillingVO checkBilling = new CheckBillingVO();
    checkBilling.setTotalAmount(totalAmount);
    checkBilling.setTotalUsage(totalUsage);
    checkBilling.setRemainAmount(remainAmount);


    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", checkBilling);
  }
}
