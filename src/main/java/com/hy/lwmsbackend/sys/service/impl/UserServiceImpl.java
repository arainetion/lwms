package com.hy.lwmsbackend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.lwmsbackend.sys.pojo.User;
import com.hy.lwmsbackend.sys.pojo.WarehouseInfo;
import com.hy.lwmsbackend.sys.mapper.UserMapper;
import com.hy.lwmsbackend.sys.service.IUserService;
import com.hy.lwmsbackend.sys.service.IWarehouseInfoService;
import com.hy.lwmsbackend.utils.PageUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Resource
    private IWarehouseInfoService warehouseInfoService;

    @Resource
    private UserMapper userMapper;

    private User queryByAuthenticatiedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        assert StringUtils.isNotEmpty(name) : "当前登录用户为非法用户";
        return this.queryByUserNo(name).get(0);
    }

    @Override
    public List<User> queryByUserNo(String no) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(no), "no", no);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public PageUtils queryByUserNoAndReturnByAuthority(String no, int pageIndex, int pageSize) {

        //通过用户名查询返回
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(no), "no", no);
        List<User> userList = this.list(queryWrapper);
        //通过权限返回list

        User currentUser = queryByAuthenticatiedUser();
        //1.获取登录用户的roleId
        Integer roleId = currentUser.getRoleId();
        String warehouseLocation = currentUser.getWarehouseLocation();

        //2.根据roleId返回PageUtils
        if (roleId == 0) { //可以查看所有用户
            return new PageUtils(userList, userList.size(), pageSize, pageIndex);
        } else if (roleId == 2) { //可以查看同仓库同事，和仓库经理
            List<User> userList2 = userList.stream()
                    .filter(user -> {
                        //a 同仓库普通员工
                        boolean a = user.getRoleId() == 2 && warehouseLocation.equals(user.getWarehouseLocation());
                        //b 仓库经理
                        boolean b = warehouseLocation.equals(user.getWarehouseLocation());
                        return a || b;
                    })
                    .collect(Collectors.toList());
            return new PageUtils(userList2, userList2.size(), pageSize, pageIndex);
        } else { //可以查看同级和对应仓库的下属
            List<User> userList1 = userList.stream()
                    .filter(user -> {
                        //同级
                        boolean a = user.getRoleId() == 1;
                        //下属
                        boolean b = warehouseLocation.equals(user.getWarehouseLocation()) && roleId < user.getRoleId();
                        return a || b;
                    })
                    .collect(Collectors.toList());
            return new PageUtils(userList1, userList1.size(), pageSize, pageIndex);
        }
    }

    /**
     * 用户账户唯一通过校验表单实现
     * 开启事务，保持用户表和仓库表经理的一致性
     *
     * @param user
     * @return
     */

    @Override
    public boolean MySaveOrUpdate(User user) {

        User currentUser = queryByAuthenticatiedUser();

        //用户id为0或者为null，则新增
        if (user.getId() == null || user.getId() == 0) {
            //业务逻辑已在表单验证中实现
            //当前登录的是经理，只能在自己仓库新增
            if (currentUser.getRoleId() == 1) {
                if (currentUser.getWarehouseLocation().equals(user.getWarehouseLocation())) {
                    return this.save(user);
                }
                return false;
            }
            return this.save(user);
        } else {
            // 根据roleId确定修改权限
            Integer roleId = currentUser.getRoleId();
            //如果 当前登录用户 roleId > 要修改的 user.getRoleId则不能修改
            if (roleId < user.getRoleId()) {
                //满足权限的前提下修改roleId和仓库
                if (!updateIdAndWarehouseLocation(user, currentUser)) {//调动失败
                    return false;
                }
                //修改保证账号唯一性(新增时通过表单验证保证)
                //roleId和warehouseLocation已经更新过
                user.setRoleId(null);
                user.setWarehouseLocation(null);
                return updateEnsureAccountUnique(user);
            } else if (roleId.equals(user.getRoleId()) && currentUser.getNo().equals(user.getNo())) {
                //roleId和仓库位置字段没有加@TableField(fill = FieldFill.INSERT_UPDATE)，将字段设置为null，则更新时不会更新相应字段
                user.setRoleId(null);
                user.setWarehouseLocation(null);
                return this.updateById(user);//仅可修改自身
            } else {
                return false;
            }
        }
    }

    //    @Transactional
    public boolean updateIdAndWarehouseLocation(User user, User currentUser) {
        //如果修改经理信息，同步更新仓库信息中的经理信息
        //roleId和仓库的更新是单独更新的，根据账号查询仓库表中的经理是否是当前更新账号
        String manger = warehouseInfoService.checkWarehouseLocation(user.getWarehouseLocation());
        List<WarehouseInfo> warehouseInfoList = warehouseInfoService.getByManger(manger);
        WarehouseInfo warehouseInfoFilterByWarehouseLocation = null;
        for (WarehouseInfo warehouseInfo : warehouseInfoList) {
            if (warehouseInfo.getWarehouseLocation().equals(user.getWarehouseLocation())) {
                warehouseInfoFilterByWarehouseLocation = warehouseInfo;
            }
        }
        Integer roleId = this.baseMapper.selectById(user.getId()).getRoleId();//通过id不变得到修改前的roleId
//        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);//新发起一个事务
//        TransactionStatus status = transactionManager.getTransaction(def);//获得事务状态
        try {
            if (!manger.equals("fail") && currentUser.getRoleId() < roleId) {//仓库经理职位空缺并且防止经理调低其他经理的roleId
                if (user.getRoleId() == 2) {//要么是降职
                    //更新仓库信息中的经理信息
                    assert warehouseInfoFilterByWarehouseLocation != null;
                    warehouseInfoFilterByWarehouseLocation.setManager("暂无经理");
                    updateManger(warehouseInfoFilterByWarehouseLocation);
                } else {
                    assert warehouseInfoFilterByWarehouseLocation != null;
                    if (warehouseInfoFilterByWarehouseLocation.getWarehouseLocation().equals(user.getWarehouseLocation()) && roleId.equals(user.getRoleId())) {
                        //要么是换工作地点，换的地方如果有经理,校验不会通过。换的地方没有经理
                        //根据换的地方查到仓库记录
                        WarehouseInfo warehouseInfo1 = warehouseInfoService.getByWarehouseLocation(user.getWarehouseLocation());
                        //更新仓库经理
                        warehouseInfo1.setManager(user.getNo());
                        updateManger(warehouseInfo1);
                    } else {//升职和换工作地点升职
                        warehouseInfoFilterByWarehouseLocation.setManager(user.getNo());
                        updateManger(warehouseInfoFilterByWarehouseLocation);
                    }
                }
                //更新user表的rolId 和 位置
                return this.updateById(user);

            }
//            transactionManager.commit(status);//手动提交事务
        } catch (Exception e) {
            //异常时候回滚
//            transactionManager.rollback(status);
        }
        return false;
    }

    private boolean updateEnsureAccountUnique(User user) {//id修改前后不变

        String no = this.baseMapper.selectById(user.getId()).getNo();//修改前
        if (!no.equals(user.getNo())) {//查询修改前该id的账号与提交的账号不一致
            //提交的用户账号在数据库中能否查询到(忽略用户自身账号存在数据库)
            List<User> userList = this.queryByUserNo(user.getNo());
//            if (userList.size() > 0) {//说明想修改账号在数据库中已存在
            if (userList.size() == 0) {
                return this.updateById(user);
            } else {
                return false;
            }
        }
        return this.updateById(user);
    }

    /**
     * 更新仓库经理
     *
     * @param warehouseInfo
     */
    private void updateManger(WarehouseInfo warehouseInfo) {

        WarehouseInfo warehouseInfo1 = new WarehouseInfo(
                warehouseInfo.getId(),
                warehouseInfo.getWarehouseLocation(),
                warehouseInfo.getName(),
                warehouseInfo.getRemark(),
                warehouseInfo.getManager()
        );
        warehouseInfoService.updateById(warehouseInfo1);
    }

    /**
     * 根据权限删除，若删除的是经理，则更新仓库信息表中经理信息
     *
     * @param user
     * @return
     */
    @Override
    public boolean removeByIdAndRoleId(User user) {

        User currentUser = queryByAuthenticatiedUser();

        //获取登录用户roleId
        Integer currentUserRoleId = currentUser.getRoleId();
        if (currentUserRoleId == 0) {//是超级管理员 任意删除，除自己外
            if (user.getRoleId() == 0) {
                return false;
            } else if (user.getRoleId() == 1) {//删除的是经理
                //根据仓库位置和账号查询仓库信息
                QueryWrapper<WarehouseInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("manager", user.getNo());
                queryWrapper.eq("warehouse_location", user.getWarehouseLocation());
                try {
                    WarehouseInfo warehouseInfo = warehouseInfoService.getOne(queryWrapper);
                    //更新仓库信息中的经理信息
                    warehouseInfo.setManager("暂无经理");
                    warehouseInfoService.updateById(warehouseInfo);
                    return this.removeById(user.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                return this.removeById(user.getId());
            }
        } else if (currentUserRoleId == 1) {//是经理，只可以删除下属
            if (user.getRoleId() == 2) {
                return this.removeById(user.getId());
            }
            return false;
        } else //普通员工不能删除
            return false;
    }

    /**
     * 新增或修改检查用户roleId是否合法
     *
     * @param roleId
     * @return 所属仓库经理
     */
    @Override
    public Map<String, Object> checkRoleId(Integer roleId, String warehouseLocation, String no) {

        User currentUser = queryByAuthenticatiedUser();

        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, String> value = new HashMap<>();
        Integer currentUserRoleId = currentUser.getRoleId();
        String manager = warehouseInfoService.checkWarehouseLocation(warehouseLocation);
        if (roleId > 2 || roleId <= 0) {
            value.put("msg", "用户角色非法");
            map.put("fail", value);
            return map;
        } else {
            if (roleId > currentUserRoleId) {//每个仓库只能有一个经理
                if (roleId == 1) {//根据仓库查询当前仓库是否有经理
                    if (StringUtils.isNotEmpty(manager) && manager.equals(no) || manager.equals("暂无经理")) {
                        value.put("msg", manager);
                        map.put("success", value);
                        return map;
                    }
                    value.put("msg", manager);
                    map.put("fail", value);
                    return map;
                }
                value.put("msg", manager);
                map.put("success", value);
                return map;
            }
        }
        value.put("msg", "权限不足");
        map.put("fail", value);
        return map;
    }

    @Override
    public PageUtils queryById(String userId) {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(User::getId, Integer.valueOf(userId));
        }
        List<User> userList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(userList, userList.size(), true);
    }

    @Override
    public PageUtils listNoPage(String no) {

        //通过权限返回list
        User currentUser = queryByAuthenticatiedUser();
        //1.获取登录用户的roleId
        Integer roleId = currentUser.getRoleId();
        String warehouseLocation = currentUser.getWarehouseLocation();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>().ge(User::getRoleId, roleId);
        if (!("全国".equals(warehouseLocation))) {
            queryWrapper.eq(User::getWarehouseLocation, warehouseLocation);
        }
        //通过用户名查询返回
        List<User> userList = this.list(queryWrapper);


        //2.根据roleId返回PageUtils
        if (roleId == 0) { //可以查看所有用户
            return new PageUtils(userList, userList.size(), true);
        } else if (roleId == 2) { //可以查看同仓库同事，和仓库经理
            List<User> userList2 = userList.stream()
                    .filter(user -> {
                        //a 同仓库普通员工
                        return user.getRoleId() == 2 && warehouseLocation.equals(user.getWarehouseLocation());
                    })
                    .collect(Collectors.toList());
            return new PageUtils(userList2, userList2.size(), true);
        } else { //可以查看同级和对应仓库的下属
            List<User> userList1 = userList.stream()
                    .filter(user -> {
                        //自己
                        boolean a = user.getNo().equals(currentUser.getNo());
                        //下属
                        boolean b = warehouseLocation.equals(user.getWarehouseLocation()) && roleId < user.getRoleId();
                        return a || b;
                    })
                    .collect(Collectors.toList());
            return new PageUtils(userList1, userList1.size(), true);
        }
    }

    @Override
    public User listByNo(String no) {

       return this.userMapper.listByNo(no);
    }

}
