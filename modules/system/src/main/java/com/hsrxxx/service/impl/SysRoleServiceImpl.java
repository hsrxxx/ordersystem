package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.constant.UserConstants;
import com.hsrxxx.common.core.exception.CustomException;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.entity.SysRole;
import com.hsrxxx.entity.SysRoleMenu;
import com.hsrxxx.entity.SysUserRole;
import com.hsrxxx.mapper.SysRoleMapper;
import com.hsrxxx.mapper.SysRoleMenuMapper;
import com.hsrxxx.mapper.SysUserRoleMapper;
import com.hsrxxx.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;


    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role)
    {
        // 新增角色信息
        roleMapper.insert(role);
        return insertRoleMenu(role);
    }

    /**
     * 更新角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role)
    {
        // 修改角色信息
        roleMapper.updateById(role);
        // 删除角色与菜单关联
        QueryWrapper<SysRoleMenu> query = new QueryWrapper<>();
        query.eq("role_id", role.getId());
        roleMenuMapper.delete(query);
        // 添加角色与菜单关联
        return insertRoleMenu(role);
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleById(Long roleId)
    {
        // 删除角色与菜单关联
        QueryWrapper<SysRoleMenu> query = new QueryWrapper<>();
        query.eq("role_id", roleId);
        roleMenuMapper.delete(query);
        return roleMapper.deleteById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(List<Long> roleIds)
    {
        for (Long roleId : roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = getById(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new CustomException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        for (Long roleId : roleIds) {
            // 删除角色与菜单关联
            QueryWrapper<SysRoleMenu> query = new QueryWrapper<>();
            query.eq("role_id", roleId);
            roleMenuMapper.delete(query);
        }
        return roleMapper.deleteBatchIds(roleIds);
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateById(role);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getId()) ? -1L : role.getId();
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        query.eq("role_name", role.getRoleName());
        SysRole info = roleMapper.selectOne(query);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getId()) ? -1L : role.getId();
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        query.eq("role_key", role.getRoleKey());
        SysRole info = roleMapper.selectOne(query);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getId()) && role.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        QueryWrapper<SysUserRole> query = new QueryWrapper<>();
        query.eq("role_id", roleId);
        return userRoleMapper.selectCount(query);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            for (SysRoleMenu srm: list) {
                rows = roleMenuMapper.insert(srm);
            }
        }
        return rows;
    }
}
