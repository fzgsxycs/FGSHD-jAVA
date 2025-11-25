package com.example.System.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    
    /**
     * 需要的权限代码
     */
    String[] value() default {};
    
    /**
     * 需要的角色代码
     */
    String[] roles() default {};
    
    /**
     * 权限验证模式
     * ANY: 满足任一权限即可
     * ALL: 需要满足所有权限
     */
    Logical logical() default Logical.ANY;
    
    enum Logical {
        ANY, ALL
    }
}