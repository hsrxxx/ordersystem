package com.hsrxxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hsrxxx.common.core.constant.UserConstants;
import com.hsrxxx.common.core.exception.CustomException;
import com.hsrxxx.common.core.utils.SecurityUtils;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.entity.SysRole;
import com.hsrxxx.entity.SysUser;
import com.hsrxxx.entity.SysUserRole;
import com.hsrxxx.entity.vo.UserRoleVO;
import com.hsrxxx.mapper.SysRoleMapper;
import com.hsrxxx.mapper.SysUserMapper;
import com.hsrxxx.mapper.SysUserRoleMapper;
import com.hsrxxx.service.SysConfigService;
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

    @Autowired
    private SysConfigService configService;

    /**
     * 根据条件分页查询用户
     *
     * @param SysUser 用户查询信息
     * @return 用户列表
     */
    @Override
    public List<SysUser> selectUserList(SysUser user) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.like(StringUtils.isNotNull(user.getUsername()), "username", user.getUsername());
        query.like(StringUtils.isNotNull(user.getTelephone()), "telephone", user.getTelephone());
        query.like(StringUtils.isNotNull(user.getStatus()), "status", user.getStatus());
        if (!user.getParams().isEmpty()){
            query.between("create_time", user.getParams().get("beginTime"), user.getParams().get("endTime"));
        }
        return userMapper.selectList(query);
    }

    /**
     * 获取用户角色数据
     *
     * @param username 用户名
     * @return 用户角色信息
     */
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

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateById(user);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateById(user);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
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

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
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

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
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

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public int updateUser(SysUser user) {
        QueryWrapper<SysUserRole> query = new QueryWrapper<>();
        query.eq("user_id",user.getId());
        userRoleMapper.delete(query);
        insertRole(user);
        return userMapper.updateById(user);
    }

    @Override
    public Long getUserIdByUserName(String userName) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username",userName);
        SysUser user = userMapper.selectOne(query);
        return user.getId();
    }

    /**
     * 新增用户信息
     * @param user
     * @return
     */
    @Override
    public int insertUser(SysUser user) {
        int rows = userMapper.insert(user);
        // 添加用户与角色管理
        insertRole(user);
        return rows;
    }

    /**
     * 新增角色信息
     * @param user
     * @return
     */
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

    /**
     * 导入用户数据
     *
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                QueryWrapper<SysUser> query = new QueryWrapper<>();
                query.eq("username", user.getUsername());
                SysUser u = userMapper.selectOne(query);
                if (StringUtils.isNull(u))
                {
                    user.setPassword(SecurityUtils.encryptPassword(StringUtils.isNull(user.getPassword()) ? password : user.getPassword()));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUsername() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUsername() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUsername() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUsername() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
