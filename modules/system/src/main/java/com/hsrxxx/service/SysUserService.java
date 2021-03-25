package com.hsrxxx.service;

import com.hsrxxx.entity.SysRole;
import com.hsrxxx.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsrxxx.entity.vo.UserRoleVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 松仁
 * @since 2021-03-08
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据条件分页查询用户
     *
     * @param SysUser 用户查询信息
     * @return 用户列表
     */
    List<SysUser> selectUserList(SysUser user);

    /**
     * 获取用户角色数据
     *
     * @param username 用户名
     * @return 用户角色信息
     */
    UserRoleVO getUserRoleByUsername(String username);

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkUserNameUnique(SysUser user);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkTelephoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    void checkUserAllowed(SysUser user);


    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    int resetPwd(SysUser user);

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUserStatus(SysUser user);

    /**
     * 新增用户信息
     * @param user
     * @return
     */
    int insertUser(SysUser user);

    /**
     * 新增角色信息
     * @param user
     * @return
     */
    void insertRole(SysUser user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int updateUser(SysUser user);
}
