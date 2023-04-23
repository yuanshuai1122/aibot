package com.aibot.utils;

import java.math.BigInteger;

/**
 * @program: aibot-server
 * @description: 通用工具类
 * @author: yuanshuai
 * @create: 2023-04-23 13:19
 **/
public class CommonUtils {

    /**
     * 大数字转换字节流（字节数组）型数据
     *
     * @param n
     * @return
     */
    public static byte[] byteConvert32Bytes(BigInteger n)
    {
        byte tmpd[] = (byte[])null;
        if(n == null)
        {
            return null;
        }

        if(n.toByteArray().length == 33)
        {
            tmpd = new byte[32];
            System.arraycopy(n.toByteArray(), 1, tmpd, 0, 32);
        }
        else if(n.toByteArray().length == 32)
        {
            tmpd = n.toByteArray();
        }
        else
        {
            tmpd = new byte[32];
            for(int i = 0; i < 32 - n.toByteArray().length; i++)
            {
                tmpd[i] = 0;
            }
            System.arraycopy(n.toByteArray(), 0, tmpd, 32 - n.toByteArray().length, n.toByteArray().length);
        }
        return tmpd;
    }


    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b
     *            byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static String byteToHex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
}
