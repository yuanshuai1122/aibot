package com.aibot.filter;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.aibot.beans.entity.ResponseResult;
import com.aibot.constants.enums.ResultCode;
import com.aibot.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * JWT过滤器，拦截 /secure的请求
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:35
 */
@Slf4j
@WebFilter(filterName = "JwtFilter", urlPatterns = "/*")
public class JwtFilter implements Filter
{
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) req;
    final HttpServletResponse response = (HttpServletResponse) res;

    response.setCharacterEncoding("UTF-8");
    //获取 header里的token
    final String token = request.getHeader("authorization");

    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
      chain.doFilter(request, response);
    }

    // 如果是白名单路径，直接继续执行
    if (isPermitted(request.getRequestURI())) {
      response.setStatus(HttpServletResponse.SC_OK);
      chain.doFilter(request, response);
    }

    // Except OPTIONS, other request should be checked by JWT
    else {

      if (token == null) {
        response.getWriter().write(JSON.toJSONString(new ResponseResult<String>(ResultCode.NOT_PERMISSION.getCode(), ResultCode.NOT_PERMISSION.getMsg())));
        return;
      }

      Map<String, Claim> userData = JwtUtil.verifyToken(token);
      if (userData == null) {
        response.getWriter().write(JSON.toJSONString(new ResponseResult<String>(ResultCode.NOT_PERMISSION.getCode(), ResultCode.NOT_PERMISSION.getMsg())));
        return;
      }
      Integer id = userData.get("id").asInt();
      String account = userData.get("account").asString();
      String password= userData.get("password").asString();
      String userParentId = userData.get("userParentId").asString();
      String shareCode = userData.get("shareCode").asString();
      //拦截器 拿到用户信息，放到request中
      request.setAttribute("id", id);
      request.setAttribute("account", account);
      request.setAttribute("password", password);
      request.setAttribute("userParentId", userParentId);
      request.setAttribute("shareCode", shareCode);
      chain.doFilter(req, res);
    }
  }

  @Override
  public void destroy() {
  }

  /** 判断是否为白名单路径 **/
  private boolean isPermitted(String requestURI) {
    return requestURI.endsWith("/user/login") || requestURI.endsWith("/user/register");
  }

}
