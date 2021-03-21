package com.hsrxxx.component;

import cn.hutool.json.JSONUtil;
import com.hsrxxx.common.core.web.domain.AjaxResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 自定义返回结果：没有登录或token过期时
 * Created by macro on 2020/6/18.
 */
@Component
public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = null;
        if ("Not Authenticated".equals(e.getMessage())){
            body = JSONUtil.toJsonStr(AjaxResult.error(401, "令牌不能为空"));
        } else if (e.getMessage().contains("Jwt expired")){
            body = JSONUtil.toJsonStr(AjaxResult.error(401, "登录状态已过期"));
        } else if ("Unsupported algorithm of HS512".equals(e.getMessage())){
            body = JSONUtil.toJsonStr(AjaxResult.error(401, "令牌验证失败"));
        }
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
