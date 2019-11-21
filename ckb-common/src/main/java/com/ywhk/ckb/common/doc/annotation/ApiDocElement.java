package com.ywhk.ckb.common.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API文档中的描述信息
 * 包括服务接口描述、调用方法描述、参数描述
 * @author zhurh
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
public @interface ApiDocElement {
	/**
	 * 字段描述
	 * @return
	 */
	String value();

	/**
	 * 是否必输
	 * @return
	 */
	boolean required() default true;

	/**
	 * 枚举字典类
	 * @return
	 */
	Class enumClass() default Enum.class;

	/**
	 * 默认值
	 * @return
	 */
	String defaultValue() default "";
}
