package com.hsrxxx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.constant.UserConstants;
import com.hsrxxx.common.core.domain.R;
import com.hsrxxx.common.core.utils.SecurityUtils;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.common.core.web.controller.BaseController;
import com.hsrxxx.common.core.web.domain.AjaxResult;
import com.hsrxxx.common.core.web.page.TableDataInfo;
import com.hsrxxx.common.security.annotation.PreAuthorize;
import com.hsrxxx.entity.SysRole;
import com.hsrxxx.entity.SysUser;
import com.hsrxxx.entity.SysUserRole;
import com.hsrxxx.service.SysPermissionService;
import com.hsrxxx.service.SysRoleService;
import com.hsrxxx.service.SysUserRoleService;
import com.hsrxxx.service.SysUserService;
import com.hsrxxx.system.api.modules.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 松仁
 * @since 2021-01-24
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 获取用户列表
     */
    @PreAuthorize(hasPermi = "system:user:list")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<SysUser> list = userService.list();
        return getDataTable(list);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize(hasPermi = "system:user:query")
    @GetMapping({"/", "/{userId}"})
    public AjaxResult query(@PathVariable(value = "userId", required = false) Long userId) {
        // 请求成功
        AjaxResult ajax =AjaxResult.success();
        // 获取角色列表
        List<SysRole> roles = roleService.list();
        // 判断用户是否超级管理员，是：返回全部角色列表，否：返回除了超级管理员外的角色列表
        ajax.put("roles", SysUser.isAdmin(userId) ? roles:roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        // 如果没有 userId 就不返回有关用户id的数据
        if (StringUtils.isNotNull(userId))
        {
            ajax.put(AjaxResult.DATA_TAG, userService.getById(userId));
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",userId);
            // 根据用户id获取角色列表
            ajax.put("roleId", userRoleService.getOne(queryWrapper).getRoleId());
        }
        return ajax;
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        SysUser sysUser = userService.getOne(queryWrapper);
        if (StringUtils.isNull(sysUser))
        {
            return R.fail("用户名或密码错误");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser.getId());
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser.getId());
        LoginUser sysUserVo = new LoginUser();
        com.hsrxxx.system.api.domain.SysUser user = new com.hsrxxx.system.api.domain.SysUser();

        BeanUtils.copyProperties(sysUser, user);
        sysUserVo.setSysUser(user);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        Long userId = SecurityUtils.getUserId();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userId);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", userService.getById(userId));
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize(hasAnyPermi = {"system:user:add", "system:user:edit"})
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user){
        // 判断用户名称是否重复
        if (StringUtils.isNotEmpty(user.getUsername())
                && UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))){
            return AjaxResult.error("新增用户'" + user.getUsername() + "'失败，该用户已存在");
        }
        else if (StringUtils.isNotEmpty(user.getTelephone())
                && UserConstants.NOT_UNIQUE.equals(userService.checkTelephoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUsername() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize(hasAnyPermi = {"system:user:add", "system:user:edit"})
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user){
        // 检查用户是否允许操作（如是超级管理员则禁止操作）
        userService.checkUserAllowed(user);
        if (StringUtils.isNotEmpty(user.getUsername())
                && UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user))){
            return AjaxResult.error("修改用户'" + user.getUsername() + "'失败，该用户已存在");
        }
        else if (StringUtils.isNotEmpty(user.getTelephone())
                && UserConstants.NOT_UNIQUE.equals(userService.checkTelephoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUsername() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize(hasPermi = "system:user:remove")
    @DeleteMapping
    public AjaxResult remove(@RequestParam("userIds") List<Long> userIds){
        for (Long userId : userIds)
        {
            userService.checkUserAllowed(new SysUser(userId));
        }
        return toAjax(userService.removeByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize(hasPermi = "system:user:edit")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }
}

