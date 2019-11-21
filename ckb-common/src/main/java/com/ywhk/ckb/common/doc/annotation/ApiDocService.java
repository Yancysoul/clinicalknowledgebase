package com.ywhk.ckb.common.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解用于标记接口服务类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ApiDocService {
    /**
     * 标记接口服务注释
     * @return
     */
    String value();
}
