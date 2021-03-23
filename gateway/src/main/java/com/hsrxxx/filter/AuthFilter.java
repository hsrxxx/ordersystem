package com.hsrxxx.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.hsrxxx.common.core.constant.CacheConstants;
import com.hsrxxx.common.core.constant.Constants;
import com.hsrxxx.common.core.domain.R;
import com.hsrxxx.common.core.utils.ServletUtils;
import com.hsrxxx.common.core.utils.StringUtils;
import com.hsrxxx.common.redis.service.RedisService;
import com.hsrxxx.config.IgnoreUrlsConfig;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 * Created by macro on 2020/6/17.
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final static long EXPIRE_TIME = Constants.TOKEN_EXPIRE * 60;

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> sops;

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String url = exchange.getRequest().getURI().getPath();
        if (StringUtils.matches(url, ignoreUrlsConfig.getUrls()))
        {
            return chain.filter(exchange);
        }
        // 检查header头内的令牌
        String token = exchange.getRequest().getHeaders().getFirst("Authorization").replace(CacheConstants.TOKEN_PREFIX, "");
        if (StrUtil.isEmpty(token)) {
            return setUnauthorizedResponse(exchange, "令牌不能为空");
        }
        // 检查redis内的令牌
        String userStr = sops.get(getTokenKey(token));
        if (StringUtils.isNull(userStr))
        {
            return setUnauthorizedResponse(exchange, "登录状态已过期");
        }
        //从token中解析用户信息并设置到Header中去
        JSONObject obj = JSONObject.parseObject(userStr);
        String username = obj.getString("username");
        String userid = obj.getString("userId");
        if (StringUtils.isBlank(userid) || StringUtils.isBlank(username))
        {
            return setUnauthorizedResponse(exchange, "令牌验证失败");
        }
        // 设置过期时间
        redisService.expire(getTokenKey(token), EXPIRE_TIME);
        // 设置用户信息到请求
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().header(CacheConstants.DETAILS_USER_ID, userid)
                .header(CacheConstants.DETAILS_USERNAME, ServletUtils.urlEncode(username)).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

        return chain.filter(mutableExchange);
    }

    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JSON.toJSONBytes(R.fail(msg)));
        }));
    }

    private String getTokenKey(String token)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
