package com.wcacg.wcgal.annotation;

import java.lang.annotation.*;

/**
 * 将接口标为需要管理员权限
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedAdmin {
}