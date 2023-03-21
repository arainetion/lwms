package com.hy.lwmsbackend.fliter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hy.lwmsbackend.constant.SystemConstant;
import com.hy.lwmsbackend.utils.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/14 10:15
 * @Description 校验token是否合法
 */

public class TokenVerifyFilter extends BasicAuthenticationFilter {
    public TokenVerifyFilter(AuthenticationManager authenticationManager) {

        super(authenticationManager);
    }

    /**
     * 校验token是否合法
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        String header = request.getHeader("Authorization");
        String s = null;
        if (header != null && header.startsWith(SystemConstant.SYS_TOKEN_PREFIX)) {
            //有token并且有添加的前缀
            //去掉前缀
            String token = header.replace(SystemConstant.SYS_TOKEN_PREFIX, "");
            //校验token是否合法
            DecodedJWT verify = JWTUtils.verify(token);
            if (verify == null) {//校验失败,提示先登录
                responseLoginInfo(response);
            } else {
                //获取当前登录信息
                String no = verify.getClaim("no").asString();
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                //根据账号获取相关权限
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(no
                        , null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            }
        } else {
            //没有携带token或者是非法请求
            responseLoginInfo(response);
        }
    }

    private void responseLoginInfo(HttpServletResponse response) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        HashMap<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("code", HttpServletResponse.SC_FORBIDDEN);
        responseInfo.put("msg", "请先登录！");
        writer.write(JSON.toJSONString(responseInfo));
        writer.flush();
        writer.close();
    }


}
