package com.hsrxxx.service.impl;

import com.hsrxxx.entity.SysUser;
import com.hsrxxx.service.SysMenuService;
import com.hsrxxx.service.SysPermissionService;
import com.hsrxxx.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: ordersystem
 * @description:
 * @author: hsrxxx
 * @create: 2021-03-11 15:57
 **/
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    @Override
    public Set<String> getRolePermission(Long userId) {

        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (userId != null && 1L == userId)
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(userId));
        }
        return roles;
    }

    @Override
    public Set<String> getMenuPermission(Long userId) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (userId != null && 1L == userId)
        {
            perms.add("*:*:*");
        }
        else
        {
            perms.addAll(menuService.selectMenuPermsByUserId(userId));
        }
        return perms;
    }
}
