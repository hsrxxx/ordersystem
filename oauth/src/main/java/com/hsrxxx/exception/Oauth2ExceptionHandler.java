package com.hsrxxx.exception;

import com.hsrxxx.common.core.web.domain.AjaxResult;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局处理Oauth2抛出的异常
 * Created by macro on 2020/7/17.
 */
@ControllerAdvice
public class Oauth2ExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public AjaxResult OAuth2Exception(OAuth2Exception e) {
        return AjaxResult.error(e.getMessage());
    }

}
