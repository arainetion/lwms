package com.hy.lwmsbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hy.lwmsbackend.constant.SystemConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/14 8:34
 * @Description JWT操作工具类
 */

@ApiModel("JWT工具类")
public class JWTUtils {

    /**
     * 生成token信息
     *
     * @param map
     * @return
     */
    public static String getToken(Map<String, String> map) {

        JWTCreator.Builder builder = JWT.create();

        //设置payload
        map.forEach(builder::withClaim);
        //设置过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);//默认过期时间为7天
        //设置header
        HashMap<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        return builder.withHeader(header)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SystemConstant.SYS_TOKEN_PREFIX));
    }

    /**
     * 不抛异常说明验证通过
     * @param token
     * @return
     */
    public static DecodedJWT verify(String token) {

        DecodedJWT verify = null;
        try {
            verify = JWT.require(Algorithm.HMAC256(SystemConstant.SYS_TOKEN_PREFIX)).build().verify(token);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return verify;
    }
}
