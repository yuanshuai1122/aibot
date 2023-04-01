package com.aibot.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理当前用户的租户ID
 */
@Component
public class TenantIdManager {
  /** 当前用户租户 KEY */
  private static final String KEY_CURRENT_TENANT_ID = "KEY_CURRENT_PROVIDER_ID";
  /** 保存当前租户ID */
  private static final ConcurrentHashMap<String, Object> TENANT_MAP = new ConcurrentHashMap<>();

  /**
   * 设置租户
   * @param tenantId 租户ID
   */
  public void setCurrentTenantId(Long tenantId) {
    TENANT_MAP.put(KEY_CURRENT_TENANT_ID, tenantId);
  }

  /**
   * 返回当前用户租户ID
   * @return
   */
  public Long getCurrentTenantId() {
    return (Long) TENANT_MAP.get(KEY_CURRENT_TENANT_ID);
  }

}
