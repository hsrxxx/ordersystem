package com.hsrxxx.domain;

import com.hsrxxx.entity.vo.UserRoleVO;
import com.hsrxxx.enums.SexEnum;
import com.hsrxxx.enums.StatusEnum;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 登录用户信息
 * Created by macro on 2020/6/19.
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态
     */
    private Boolean enabled;

    /**
     * 用户状态
     */
    private StatusEnum status;

    /**
     * 权限数据
     */
    private Collection<SimpleGrantedAuthority> authorities;

    public SecurityUser() {

    }

    public SecurityUser(UserRoleVO userRoleVO) {
        this.setId(userRoleVO.getId());
        this.setUsername(userRoleVO.getUsername());
        this.setPassword(userRoleVO.getPassword());
        this.setStatus(userRoleVO.getStatus());
        this.setEnabled(userRoleVO.getStatus() == StatusEnum.normal);
        if (userRoleVO.getRoleKey() != null) {
            this.authorities = new ArrayList<>();
            this.authorities.add(new SimpleGrantedAuthority(userRoleVO.getRoleKey()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}