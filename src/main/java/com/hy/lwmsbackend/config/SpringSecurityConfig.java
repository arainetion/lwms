package com.hy.lwmsbackend.config;

import com.hy.lwmsbackend.fliter.TokenLoginFilter;
import com.hy.lwmsbackend.fliter.TokenVerifyFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/14 10:49
 * @Description
 */

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //绑定自定义的认证Service
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());//自定义密码处理器
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()

                .authorizeRequests()
                .antMatchers("/doc.html", "/doc.html/**", "/webjars/**", "/v2/**", "/swagger-resources",
                        "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui.html/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // 设置跨域的处理
                .cors().configurationSource(configurationSource())
                .and()
                .addFilter(new TokenLoginFilter(super.authenticationManager())) // 绑定认证的接口
                .addFilter(new TokenVerifyFilter(super.authenticationManager())) // 绑定校验的接口
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //所需要用到的静态资源，允许访问
        web.ignoring().antMatchers("/doc.html",
                "/doc.html/**",
                "/swagger-ui.html",
                "/swagger-ui/*",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/webjars/**");
    }

    private CorsConfigurationSource configurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600l);
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return configurationSource;
    }


}
