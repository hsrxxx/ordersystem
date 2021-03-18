package com.hsrxxx.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 * @Target 描述注解的使用范围
 * @Retention 描述注解保留的时间范围（被描述的注解在它所修饰的类中可以被保留到何时）
 * @author ruoyi
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorize
{
    /**
     * 验证用户是否具备某权限
     */
    public String hasPermi() default "";

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     */
    public String lacksPermi() default "";

    /**
     * 验证用户是否具有以下任意一个权限
     */
    public String[] hasAnyPermi() default {};

    /**
     * 判断用户是否拥有某个角色
     */
    public String hasRole() default "";

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反
     */
    public String lacksRole() default "";

    /**
     * 验证用户是否具有以下任意一个角色
     */
    public String[] hasAnyRoles() default {};
}