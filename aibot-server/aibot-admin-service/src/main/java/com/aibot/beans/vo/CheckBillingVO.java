package com.aibot.beans.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 检查额度vo
 *
 * @author: yuanshuai
 * @create: 2023-04-02 15:18
 */
@Data
public class CheckBillingVO {

  /**
   * 总额度
   */
  private BigDecimal totalAmount;

  /**
   * 已使用额度
   */
  private BigDecimal totalUsage;

  /**
   * 剩余额度
   */
  private BigDecimal remainAmount;

}
