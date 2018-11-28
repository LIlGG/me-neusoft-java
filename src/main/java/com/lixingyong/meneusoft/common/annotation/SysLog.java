package com.lixingyong.meneusoft.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解类，用来定义全局的注解
 */

/**
 * @Target(ElementType.METHOD) 定义注解只能用在方法中
 * @Retention(RetentionPolicy.RUNTIME) 注解会在class字节码文件中存在，可以通过反射获取到
 * @Documented 注解存在于javadoc中
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
