package com.hy.lwmsbackend.fliter;

import com.alibaba.fastjson.JSON;
import com.hy.lwmsbackend.constant.SystemConstant;
import com.hy.lwmsbackend.sys.pojo.User;
import com.hy.lwmsbackend.utils.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/14 9:41
 * @Description
 */

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * 具体认证方法
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */

    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //前后端分离项目中提交的数据是JSON字符串
        try {
            String loginInfo = getRequestJSON(request);
            User user = JSON.parseObject(loginInfo, User.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getNo(), user.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getRequestJSON(HttpServletRequest request) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = bufferedReader.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 认证成功
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        //生成Token信息
        HashMap<String, String> map = new HashMap<>();
        map.put("no",authResult.getName());
        String token = JWTUtils.getToken(map);
        //token响应给客户端
        response.setHeader("Authorization", SystemConstant.SYS_TOKEN_PREFIX + token);
        response.setContentType("application/json;charset=utf-8");
        response.addHeader("Access-Control-Expose-Headers","Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        HashMap<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("code",HttpServletResponse.SC_OK);
        responseInfo.put("msg","认证通过");
        writer.write(JSON.toJSONString(responseInfo));
        writer.flush();
        writer.close();

    }

    /**
     * 认证失败
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {


        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        HashMap<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("code",HttpServletResponse.SC_UNAUTHORIZED);;
        responseInfo.put("msg","认证失败，请检查用户名或密码");
        writer.write(JSON.toJSONString(responseInfo));
        writer.flush();
        writer.close();
    }
}
