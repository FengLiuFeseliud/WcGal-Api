package com.wcacg.wcgal.annotation;

import java.lang.annotation.*;

/**
 * 将接口标为需要已登录权限
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedToken {

    /**
     * true 为需要 admin 权限
     */
    boolean admin() default false;
}
