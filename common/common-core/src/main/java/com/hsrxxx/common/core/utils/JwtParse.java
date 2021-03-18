package com.hsrxxx.common.core.utils;


import com.nimbusds.jose.JWSObject;

import java.text.ParseException;

/**
 * @program: ordersystem
 * @description: Jwt解析器
 * @author: hsrxxx
 * @create: 2021-03-08 15:10
 **/
public class JwtParse {

    public static String tokenParse(String token){
        String userStr = null;
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            userStr = jwsObject.getPayload().toString();
        } catch (ParseException e){
            e.printStackTrace();
        }
        return userStr;
    }
}
