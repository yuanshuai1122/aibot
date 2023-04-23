package com.aibot.utils;

import cn.hutool.core.codec.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @program: aibot-server
 * @description: AlipaySignature
 * @author: yuanshuai
 * @create: 2023-04-23 13:26
 **/
public class AlipaySignature {


    public static String rsaSign(String content, String privateKey, String charset, String signType) throws Exception {
        if ("RSA".equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else {
            System.out.print("Sign Type is Not Support : signType=" + signType);
            return "";
        }
    }

    public static String rsaSign(String content, String privateKey, String charset) throws Exception {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(Base64.encode(signed));
        } catch (InvalidKeySpecException var6) {
            System.out.print("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥");
            throw new Exception("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", var6);
        } catch (Exception var7) {
            throw new Exception("RSAcontent = " + content + "; charset = " + charset, var7);
        }
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins != null && !StringUtils.isEmpty(algorithm)) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            String data = StreamUtil.readText(ins);
            byte[] encodedKey = data.getBytes();
            encodedKey = Base64.decode(data);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }

    public static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) throws Exception {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(pubKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            return null != sign && signature.verify(Base64.decode(sign));
        } catch (Exception var6) {
            throw new Exception("RSAcontent = " + content + ",sign=" + sign + ",charset =" + charset, var6);
        }
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        StreamUtil.io(new InputStreamReader(ins), writer);
        String data = writer.toString();
        byte[] encodedKey = data.getBytes();
        encodedKey = Base64.decode(data);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

}
