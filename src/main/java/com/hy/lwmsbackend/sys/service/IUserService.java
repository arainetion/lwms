package com.hy.lwmsbackend.sys.service;

import com.hy.lwmsbackend.sys.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hy.lwmsbackend.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
public interface IUserService extends IService<User> {



    List<User> queryByUserNo(String no);

    PageUtils queryByUserNoAndReturnByAuthority(String no, int pageIndex, int pageSize);

    boolean MySaveOrUpdate(User user);

    boolean removeByIdAndRoleId(User user);

    Map<String, Object> checkRoleId(Integer roleId, String warehouseLocation, String no);

    PageUtils queryById(String userId);
}
