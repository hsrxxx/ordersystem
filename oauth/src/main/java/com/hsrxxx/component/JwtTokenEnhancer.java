package com.hsrxxx.component;

import com.hsrxxx.domain.SecurityUser;
import com.hsrxxx.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JWT内容增强器
 * Created by macro on 2020/6/19.
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Map<String, Object> userInfo = new HashMap<>();
        //把用户ID设置到JWT中
        Long userId = securityUser.getId();
        Set<String> roles = permissionService.getRolePermission(userId);
        Set<String> permsSet = permissionService.getMenuPermission(userId);
        userInfo.put("userId", userId);
        userInfo.put("roles", roles);
        userInfo.put("permsSet", permsSet);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(userInfo);
        return accessToken;
    }
}
