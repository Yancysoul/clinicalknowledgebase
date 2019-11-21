package com.ywhk.ckb.common.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解用于标记应用接口
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiDocInterface {

    /**
     * 填写接口的注释描述信息
     * @return
     */
    String value();
}
