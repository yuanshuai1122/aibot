package com.aibot.interceptor;

import com.aibot.beans.entity.TenantInfo;
import com.aibot.config.TenantIdManager;
import com.aibot.constants.RegConstants;
import com.aibot.constants.enums.ResultCode;
import com.aibot.exception.CommonException;
import com.aibot.mapper.TenantInfoMapper;
import com.aibot.utils.JwtUtil;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jwt拦截器
 *
 * @author: yuanshuai
 * @create: 2023-04-09 15:12
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {


  @Autowired
  private TenantIdManager tenantIdManager;

  @Autowired
  private TenantInfoMapper tenantInfoMapper;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {

    response.setCharacterEncoding("UTF-8");
    //获取 header里的token
    final String token = request.getHeader("authorization");

    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
      return true;
    }

    // Except OPTIONS, other request should be checked by JWT
    else {

      if (token == null) {
        throw new CommonException(ResultCode.UNAUTHENTICATED);
      }

      Map<String, Claim> userData = JwtUtil.verifyToken(token);
      if (userData == null) {
        throw new CommonException(ResultCode.TOKEN_LOSE_EFFICACY);
      }

      // 获取请求url
      String url = request.getHeader("referer");
      if (StringUtils.isBlank(url)) {
        log.info("租户不存在，请联系管理员");
        throw new CommonException(ResultCode.UNAUTHORISED);
      }
      log.info("请求url为： {}", url);
      String domain = "";
      Pattern p = Pattern.compile(RegConstants.URL_REG);
      Matcher m = p.matcher(url);
      if (m.find()) {
        domain = m.group(1);
      }

      // TODO 这里写死localhost 为了测试
      domain = "localhost";

      // 查询站点租户id
      QueryWrapper<TenantInfo> wrapper = new QueryWrapper<>();
      wrapper.lambda().eq(TenantInfo::getTenantHost, domain);
      TenantInfo tenantInfo = tenantInfoMapper.selectOne(wrapper);
      if (null == tenantInfo) {
        throw new CommonException(ResultCode.UNAUTHORISED);
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
      return true;
    }



  }

}
