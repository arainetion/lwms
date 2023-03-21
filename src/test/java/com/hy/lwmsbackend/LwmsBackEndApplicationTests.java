package com.hy.lwmsbackend;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hy.lwmsbackend.constant.SystemConstant;
import com.hy.lwmsbackend.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class LwmsBackEndApplicationTests {

    @Test
    @DisplayName("token生成")
    void generatorToken() {

        HashMap<String, String> map = new HashMap<>();
        map.put("lz","768928");
        String token = JWTUtils.getToken(map);
        System.out.println(token);
    }

    @Test
    @DisplayName("校验token")
    void verifier() {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzkzNzYxMzUsImx6IjoiNzY4OTI4In0.ZL_Idr2rYVcaQeVb6l7ZkwOP2X80sW52Pph8gdXAyiE";
        DecodedJWT verify = JWTUtils.verify(token);
        System.out.println(verify.toString());
    }

    @Test
    void assertTest(){

        HashMap<String, String> map = new HashMap<>();
        map.put("msg", "帅哥");
        map.put("ds","fail");
        String msg = map.get("msg");
        System.out.println(msg);
    }

}
