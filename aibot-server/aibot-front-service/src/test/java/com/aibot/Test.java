package com.aibot;

import com.aibot.beans.dto.JourneyCreateDTO;
import com.aibot.constants.RegConstants;
import com.aibot.service.ImagesService;
import com.aibot.utils.ValueUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ss
 *
 * @author: aabb
 * @create: 2023-03-21 21:38
 */
public class Test {

  public static void main(String[] args) {
    String phoneReg = RegConstants.PHONE_REG;
    boolean matches = "10232336174".matches(phoneReg);
    System.out.println(matches);
  }
}
