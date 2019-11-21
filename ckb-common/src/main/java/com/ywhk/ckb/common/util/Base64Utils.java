package com.ywhk.ckb.common.util;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.Base64;

/**
 * @author zhuronghui
 * @date 2019年08月29日
 * @description
 */
public final class Base64Utils {
    /**
     * 字符串编码成base64
     * @param str
     * @return
     */
    @SneakyThrows
    public static String encodeBase64(String str){
        if(StringUtils.isEmpty(str)){
            return str;
        }
        byte[] base64Byte = Base64.getEncoder().encode(str.getBytes("UTF-8"));
        return new String(base64Byte);
    }

    /**
     * base64解码成字符串
     * @param base64
     * @return
     */
    public static String decodeBase64(String base64){
        if(StringUtils.isEmpty(base64)){
            return base64;
        }
        byte[] strByte = Base64.getDecoder().decode(base64);
        return new String(strByte);
    }
    /**
     * 解码
     * @param encodedText
     * @return
     */
    public static byte[] decode(String encodedText){
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }
    /**
     * 编码
     * @param data
     * @return
     */
    public static String encode(byte[] data){
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }
}
