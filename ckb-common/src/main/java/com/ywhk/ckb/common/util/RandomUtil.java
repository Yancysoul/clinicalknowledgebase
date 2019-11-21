package com.ywhk.ckb.common.util;

import java.util.Random;

public class RandomUtil {
	/**
     * 生成随机数当作getItemID
     * n ： 需要的长度
     * @return
     */
    public static String getItemPassWd(int n)
    {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for ( int i = 0; i < n; i++ )
        {
            String str = random.nextInt( 2 ) % 2 == 0 ? "num" : "char";
            if ( "char".equalsIgnoreCase( str ) )
            { // 产生字母
                int nextInt = random.nextInt( 2 ) % 2 == 0 ? 65 : 97;
                val.append((char) ( nextInt + random.nextInt( 26 ) ));
            }
            else if ( "num".equalsIgnoreCase( str ) )
            { // 产生数字
                val.append(String.valueOf( random.nextInt( 10 ) ));
            }
        }
        return val.toString();
    }
}
