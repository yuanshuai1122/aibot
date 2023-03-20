package com.chatgpt.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP相关工具类
 *
 * @author: yuanshuai
 * @create: 2023-03-03 16:48
 */
public class IPUtils {

  /**
   * 获取用户真实ip
   * @param request request
   * @return 用户ip
   */
  public static String getIpAddr(HttpServletRequest request)
  {
    if (request == null)
    {
      return "unknown";
    }
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("X-Forwarded-For");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getHeader("X-Real-IP");
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getRemoteAddr();
    }

    return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
  }


}
