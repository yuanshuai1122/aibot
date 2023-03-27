package com.aibot.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 蜜堂工具类
 */
public class MtUtils {
    private static final String PREFIX = "mce-auth-v1";
    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static String CHARSET = "UTF-8";

    /**
     * 签名：生成认证字符串 mce-auth-v1/{appId}/{timeStamp}/{expire}/{sign}
     * @param appId 用户id
     * @param secretKey 用户秘钥
     * @param datetime 签名时间
     * @param expire 签名过期时间
     * @param requestParam 请求参数
     * @return 签名认证字符串
     */
    public static String sign(String appId, String secretKey, Date datetime, int expire, Map<String,? extends Object> requestParam){
        // 生成时间戳
        String timeStamp = new SimpleDateFormat(FORMAT).format(datetime);
        //
        StringBuilder signStr = new StringBuilder(PREFIX).append("/")
                .append(appId).append("/")
                .append(timeStamp).append("/")
                .append(expire);
        // 生成派生秘钥
        String signKey = hmacHSA256(secretKey, signStr.toString());
        // 请求参数规范化
        SortedMap<String, Object> sortMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sortMap.putAll(requestParam);
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortMap.entrySet()) {
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();
            if (paramValue == null || "".equals(paramValue.toString()) || "null".equals(paramValue.toString())) {
                continue;
            }
            if (content.length()>0) {
                content.append("&");
            }
            content.append(paramName).append("=").append(paramValue);
        }
        // 生成签名
        String sign = hmacHSA256(signKey, content.toString());
        //
        signStr.append("/").append(sign);
        return signStr.toString();
    }

    /**
     * 加密请求数据
     */
    public static String encrypt(byte[] data, String key) {
        if (key.length() < 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        } else if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            IvParameterSpec ivspec = new IvParameterSpec("\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);// 初始化
            byte[] result = cipher.doFinal(data);
            return Base64.encodeBase64String(result);
        } catch (Exception e){
            throw new RuntimeException("encrypt failed!");
        }
    }

    /**
     * 解密
     * @param data 待解密内容(十六进制字符串)
     * @param key  加密秘钥
     */
    public static String decrypt(String data, String key) throws Exception {
        System.out.println(data);
        if (key.length() < 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        } else if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        byte[] decryptFrom;
        decryptFrom = Base64.decodeBase64(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(CHARSET), "AES");
        IvParameterSpec ivspec = new IvParameterSpec("\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);// 初始化
        byte[] result = cipher.doFinal(decryptFrom);
        return new String(result, CHARSET);
    }

    /**
     * 发送请求
     */
    public static HttpResponse doPost(String url, Map<String, String> headers, String body) throws Exception {
        HttpClient httpClient = wrapClient(url);

        HttpPost request = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }


    private static String hmacHSA256(String key, String content){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return hex(bytes);
        } catch (Exception e){
            throw new RuntimeException("签名生成错误！",e);
        }
    }

    private static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            int decimal = (int) aByte & 0xff;
            // get last 8 bits
            String hex = Integer.toHexString(decimal);
            if (hex.length() % 2 == 1) {
                hex = "0" + hex;
            }
            result.append(hex);
        }
        return result.toString();
    }

    private static HttpClient wrapClient(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        if (url.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }


    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }
                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
