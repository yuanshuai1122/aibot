package com.aibot.filter;

import com.aibot.beans.entity.ResponseResult;
import com.aibot.beans.entity.TenantInfo;
import com.aibot.config.TenantIdManager;
import com.aibot.mapper.TenantInfoMapper;
import com.aibot.utils.JwtUtil;
import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * JWT过滤器，拦截 /secure的请求
 *
 * @author: yuanshuai
 * @create: 2023-03-20 11:35
 */
@Slf4j
@WebFilter(filterName = "JwtFilter", urlPatterns = "/*", asyncSupported = true)
public class JwtFilter implements Filter {

  @Autowired
  private TenantIdManager tenantIdManager;

  @Autowired
  private TenantInfoMapper tenantInfoMapper;


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
        response401(response, "请登录");
        return;
      }

      Map<String, Claim> userData = JwtUtil.verifyToken(token);
      if (userData == null) {
        response401(response, "登录已失效，请重新登录");
        return;
      }

      // 获取请求url
      String serverName = request.getServerName();
      log.info("请求url为： {}", serverName);

      // 查询站点租户id
      QueryWrapper<TenantInfo> wrapper = new QueryWrapper<>();
      wrapper.lambda().eq(TenantInfo::getTenantHost, serverName);
      TenantInfo tenantInfo = tenantInfoMapper.selectOne(wrapper);
      if (null == tenantInfo) {
        response401(response, "站点异常，请稍后再试");
        return;
      }

      Integer id = userData.get("id").asInt();
      String account = userData.get("account").asString();
      String password = userData.get("password").asString();
      String userParentId = userData.get("userParentId").asString();
      String shareCode = userData.get("shareCode").asString();
      String role = userData.get("role").asString();
      Integer tenantId = tenantInfo.getTenantId();

      // 将id放入租户
      tenantIdManager.setCurrentTenantId(Long.parseLong(tenantId.toString()));

      //拦截器 拿到用户信息，放到request中
      request.setAttribute("id", id);
      request.setAttribute("account", account);
      request.setAttribute("password", password);
      request.setAttribute("userParentId", userParentId);
      request.setAttribute("shareCode", shareCode);
      request.setAttribute("role", role);
      request.setAttribute("tenantId", tenantId);
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

  /**
   * 无需转发，直接返回Response信息
   */
  private void response401(ServletResponse resp, String msg) {
    HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
    httpServletResponse.setStatus(401);
    httpServletResponse.setCharacterEncoding("UTF-8");
    httpServletResponse.setContentType("application/json; charset=utf-8");
    PrintWriter out = null;
    try {
      out = httpServletResponse.getWriter();
      String data = JSON.toJSONString(new ResponseResult<String>(401, "无权访问(Unauthorized):" + msg, null));
      out.append(data);
    } catch (IOException e) {
      log.error(e.getMessage());
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

}
