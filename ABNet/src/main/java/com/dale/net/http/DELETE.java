package com.dale.net.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Desc:delete请求
 **/
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DELETE {
    /**
     * 内容是baseUrl之外的路径
     */
    String value() default "";
}
