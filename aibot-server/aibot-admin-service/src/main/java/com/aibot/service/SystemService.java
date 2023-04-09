package com.aibot.service;

import com.aibot.beans.dto.UpdateKeyStatusDTO;
import com.aibot.beans.entity.ChatApiKey;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.vo.ChatApiKeyVO;
import com.aibot.beans.vo.CheckBillingVO;
import com.aibot.constants.enums.ResultCode;
import com.aibot.mapper.ChatApiKeyMapper;
import com.aibot.utils.DateUtils;
import com.aibot.utils.OkHttpUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

  @Autowired
  private ChatApiKeyMapper chatApiKeyMapper;

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

  /**
   * 获取api的key列表
   * @return api的key列表
   */
  public ResponseResult<List<ChatApiKeyVO>> keyList() {

    List<ChatApiKey> chatApiKeys = chatApiKeyMapper.selectList(null);

    ArrayList<ChatApiKeyVO> apiKeys = new ArrayList<>();
    for (ChatApiKey chatApiKey : chatApiKeys) {
      ChatApiKeyVO chatApiKeyVO = new ChatApiKeyVO();
      chatApiKeyVO.setId(chatApiKey.getId());
      chatApiKeyVO.setApiKey(chatApiKey.getApiKey());
      chatApiKeyVO.setAccount(chatApiKey.getAccount());
      if (chatApiKey.getStatus() == 0) {
        chatApiKeyVO.setStatus(true);
      }

      apiKeys.add(chatApiKeyVO);
    }


    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "获取成功", apiKeys);
  }

  /**
   * 更新key状态
   * @param dto key状态dto
   * @return
   */
  public ResponseResult<Object> updateKeyStatus(UpdateKeyStatusDTO dto) {

    ChatApiKey chatApiKey = new ChatApiKey();
    chatApiKey.setId(dto.getId());
    if (dto.getStatus()) {
      chatApiKey.setStatus(0);
    }else {
      chatApiKey.setStatus(1);
    }

    chatApiKeyMapper.updateById(chatApiKey);

    return new ResponseResult<>(ResultCode.SUCCESS.getCode(), "更新成功", null);

  }
}
