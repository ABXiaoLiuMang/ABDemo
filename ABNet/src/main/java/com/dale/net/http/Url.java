package com.dale.net.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Desc:在api接口的方法中的参数中使用此注解,可以输入全路径请求数据
 * 注意:用了此注解,不能在@GET 和@POST注解中传入值,否则会报异常
 **/
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Url {
}
