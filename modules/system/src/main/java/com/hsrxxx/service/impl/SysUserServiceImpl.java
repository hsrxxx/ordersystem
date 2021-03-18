package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.constant.UserConstants;
import com.hsrxxx.common.core.exception.CustomException;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.entity.SysRole;
import com.hsrxxx.entity.SysUser;
import com.hsrxxx.entity.SysUserRole;
import com.hsrxxx.entity.vo.UserRoleVO;
import com.hsrxxx.mapper.SysRoleMapper;
import com.hsrxxx.mapper.SysUserMapper;
import com.hsrxxx.mapper.SysUserRoleMapper;
import com.hsrxxx.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleMapper roleMapper;


    @Override
    public UserRoleVO getUserRoleByUsername(String username) {
        UserRoleVO userRoleVO = new UserRoleVO();

        QueryWrapper<SysUser> querySysUser = new QueryWrapper<>();
        querySysUser.eq("username",username);
        SysUser user = userMapper.selectOne(querySysUser);

        if (StringUtils.isNull(user))
        {
            throw new CustomException("用户名或密码错误");
        }

        QueryWrapper<SysUserRole> querySysUserRole = new QueryWrapper<>();
        querySysUserRole.eq("user_id", user.getId());
        SysUserRole userRole = userRoleMapper.selectOne(querySysUserRole);

        QueryWrapper<SysRole> querySysRole = new QueryWrapper<>();
        querySysRole.eq("id", userRole.getRoleId());
        SysRole role = roleMapper.selectOne(querySysRole);

        BeanUtils.copyProperties(user, userRoleVO);
        BeanUtils.copyProperties(role, userRoleVO);

        return userRoleVO;
    }

    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateById(user);
    }

    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateById(user);
    }

    @Override
    public String checkUserNameUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username", user.getUsername());
        SysUser info = userMapper.selectOne(query);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkTelephoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("telephone", user.getTelephone());
        SysUser info = userMapper.selectOne(query);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("email", user.getEmail());
        SysUser info = userMapper.selectOne(query);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int updateUser(SysUser user) {
        QueryWrapper<SysUserRole> query = new QueryWrapper<>();
        query.eq("user_id",user.getId());
        userRoleMapper.delete(query);
        insertRole(user);
        return userMapper.updateById(user);
    }

    @Override
    public int insertUser(SysUser user) {
        int rows = userMapper.insert(user);
        // 添加用户与角色管理
        insertRole(user);
        return rows;
    }

    @Override
    public void insertRole(SysUser user) {
        Long[] roleIds = user.getRoleIds();
        if (StringUtils.isNotNull(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                for (SysUserRole userRole: list)
                {
                    userRoleMapper.insert(userRole);
                }
            }
        }
    }
}
