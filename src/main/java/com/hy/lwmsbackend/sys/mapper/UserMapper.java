package com.hy.lwmsbackend.sys.mapper;

import com.hy.lwmsbackend.sys.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from lwms.user where user.no = #{no}")
    User listByNo(String no);
}
