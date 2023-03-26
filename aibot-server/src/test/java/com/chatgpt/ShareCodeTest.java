package com.chatgpt;

import com.chatgpt.utils.ShareCodeUtils;
import org.junit.jupiter.api.Test;

/**
 * 分享码测试类
 *
 * @author: yuanshuai
 * @create: 2023-03-21 11:37
 */
public class ShareCodeTest {

  @Test
  void idToCode() {
    String code = ShareCodeUtils.idToCode(2);
    System.out.println(code);
  }

  @Test
  void codeToId() {
    Integer id = ShareCodeUtils.codeToId("EA64PE");
    System.out.println(id);
  }

}
