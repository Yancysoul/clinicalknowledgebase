package com.ywhk.ckb.common.util;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public class Md5Utils {

    @SneakyThrows
    public static String getMD5(String str) {

        MessageDigest messageDigest = null;

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(str.getBytes("UTF-8"));

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1){
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            }else{
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }

        }

        return md5StrBuff.toString();
    }

}
