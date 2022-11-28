package com.sustech.ooad.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Calendar;
import java.util.Map;

/**
 * Authored By baobao555#
 * https://blog.csdn.net/weixin_45070175/article/details/118559272
 */
public class JWTUtils {


    private static final String SECRET_KEY = "0e36b215-a919-45e1-8b54-6f90d5ed219c";

    /**
     * 生成token
     * @param payload token携带的信息
     * @return token字符串
     */
    public static String getToken(Map<String,String> payload){
        // 指定token过期时间为10小时
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 10);

        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payload.forEach(builder::withClaim);
        // 指定过期时间和签名算法
        String token = builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SECRET_KEY));
        return token;
    }


    /**
     * 解析token
     * @param token token字符串
     * @return 解析后的token
     */
    public static DecodedJWT decode(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }
}

