package com.hsrxxx.service;

import com.hsrxxx.domain.SecurityUser;
import com.hsrxxx.entity.vo.UserRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户管理业务类
 * Created by macro on 2020/6/19.
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService systemUserService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserRoleVO userRoleVO = systemUserService.getUserRoleByUsername(username);
        if (StringUtils.isEmpty(userRoleVO)) {
            throw new UsernameNotFoundException("用户不存在/密码错误");
        }

        SecurityUser securityUser = new SecurityUser(userRoleVO);
        if (!securityUser.isEnabled()) {
            // 帐户已启用
            throw new DisabledException("对不起，您的账号：" + username + " 已停用");
        } else if (!securityUser.isAccountNonLocked()) {
            // 帐户未锁定
            throw new LockedException("对不起，您的账号：" + username + " 已锁定");
        } else if (!securityUser.isAccountNonExpired()) {
            // 帐户未过期
            throw new AccountExpiredException("对不起，您的账号：" + username + " 已过期");
        } else if (!securityUser.isCredentialsNonExpired()) {
            // 凭证未过期
            throw new CredentialsExpiredException("对不起，您的账号：" + username + " 凭证已过期");
        }
        // 通过则返回
        return securityUser;
    }

}
