package com.hsrxxx.controller;

import com.hsrxxx.common.core.constant.HttpStatus;
import com.hsrxxx.common.core.domain.R;
import com.hsrxxx.common.core.exception.CustomException;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.common.security.service.TokenService;
import com.hsrxxx.service.SysLoginService;
import com.hsrxxx.system.api.modules.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 * Created by macro on 2020/7/17.
 */
@RestController
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    /**
     * Oauth2登录认证
     */
    @PostMapping("/oauth/token")
    public R<?> login(Principal principal, @RequestParam Map<String, String> parameters) {

        OAuth2AccessToken oAuth2AccessToken = null;
        try {
            oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new CustomException("用户认证失败，请检查参数是否正确");
        }
        String username = parameters.get("username");
        String password = parameters.get("password");
        LoginUser userInfo = sysLoginService.login(username, password);
        Map<String, Object> map = tokenService.createToken(userInfo, oAuth2AccessToken.getValue(), oAuth2AccessToken.getRefreshToken().getValue());
        if ("password".equals(parameters.get("grant_type"))){
            return R.ok(map, "用户" + username + "登陆成功");
        } else if ("refresh_token".equals(parameters.get("grant_type"))){
            return R.ok(map, "用户" + username + "欢迎回来");
        }
        return R.fail(HttpStatus.FORBIDDEN, "授权类型无效");
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
//            String username = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
//            // 记录用户退出日志
//            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }
}
