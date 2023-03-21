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
    String code = ShareCodeUtils.idToCode(2L);
    System.out.println(code);
  }

  @Test
  void codeToId() {
    Long id = ShareCodeUtils.codeToId("EAHYZ7");
    System.out.println(id);
  }

}
