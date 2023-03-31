package com.aibot;

import com.aibot.beans.entity.User;
import com.aibot.config.TenantIdManager;
import com.aibot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AibotAdminServiceApplicationTests {

  /* 租户ID管理器 */
  @Autowired
  private TenantIdManager tenantIdManager;

  @Autowired
  private UserMapper userMapper;

  @Test
  void contextLoads() {

    findUser(1);


  }

  private void findUser(long tenantId) {
    // 设置租户ID
    tenantIdManager.setCurrentTenantId(tenantId);
    // 查询用户列表
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.lt("id", 20);
    List<User> userBeanList = userMapper.selectList(wrapper);
    for (User user : userBeanList) {
      System.out.println(user);
    }
  }

}
