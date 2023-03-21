package com.hy.lwmsbackend.sys.service.impl;

import com.hy.lwmsbackend.sys.pojo.User;
import com.hy.lwmsbackend.sys.service.IUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/14 17:14
 * @Description
 */

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Resource
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String no) throws UsernameNotFoundException {

        List<User> userList = userService.queryByUserNo(no);
        if (userList.size() != 1){
            throw new RuntimeException("用户名或者密码错误");
        }
        User user = userList.get(0);
        //如果没有查到用户就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }

        //进行加密
        String encode = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encode);

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getNo()));
        return new org.springframework.security.core.userdetails.User(user.getNo(), user.getPassword(),authorities);
    }

}
