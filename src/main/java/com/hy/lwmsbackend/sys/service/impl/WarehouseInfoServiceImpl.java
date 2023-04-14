package com.hy.lwmsbackend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.lwmsbackend.sys.pojo.Goods;
import com.hy.lwmsbackend.sys.pojo.User;
import com.hy.lwmsbackend.sys.pojo.WarehouseInfo;
import com.hy.lwmsbackend.sys.mapper.WarehouseInfoMapper;
import com.hy.lwmsbackend.sys.service.IGoodsService;
import com.hy.lwmsbackend.sys.service.IUserService;
import com.hy.lwmsbackend.sys.service.IWarehouseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.lwmsbackend.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hy
 * @since 2023-03-17
 */
@Service
public class WarehouseInfoServiceImpl extends ServiceImpl<WarehouseInfoMapper, WarehouseInfo> implements IWarehouseInfoService {

    @Lazy
    @Resource
    private IUserService userService;

    @Resource
    private IGoodsService goodsService;

    /**
     * 查询仓库经理，并返回经理账号
     *
     * @param warehouseLocation
     * @return
     */
    @Override
    public String checkWarehouseLocation(String warehouseLocation) {

        QueryWrapper<WarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(warehouseLocation), "warehouse_location", warehouseLocation);
        WarehouseInfo warehouseInfo = this.getOne(queryWrapper);
        return warehouseInfo == null ? "fail" : warehouseInfo.getManager();

    }

    /**
     * 通过经理返回一条仓库记录
     *
     * @param manger
     * @return
     */
    @Override
    public List<WarehouseInfo> getByManger(String manger) {

        QueryWrapper<WarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(manger), "manager", manger);
        return this.getBaseMapper().selectList(queryWrapper);
    }

    /**
     * 通过仓库位置返回一条仓库记录
     *
     * @param warehouseLocation
     * @return
     */
    @Override
    public WarehouseInfo getByWarehouseLocation(String warehouseLocation) {

        QueryWrapper<WarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(warehouseLocation), "warehouse_location", warehouseLocation);

        return this.getOne(queryWrapper);
    }

    /**
     * 根据仓库位置模糊查询返回仓库记录
     *
     * @param warehouseLocation
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public PageUtils queryByWarehouseLocation(String warehouseLocation, int pageIndex, int pageSize, boolean flag) {

        LambdaQueryWrapper<WarehouseInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(WarehouseInfo::getWarehouseLocation, warehouseLocation);
        List<WarehouseInfo> warehouseInfoList = this.baseMapper.selectList(lambdaQueryWrapper);
        if (!flag) {
            return new PageUtils(warehouseInfoList, warehouseInfoList.size(), pageSize, pageIndex);
        } else {
            return new PageUtils(warehouseInfoList, warehouseInfoList.size());
        }
    }

    /**
     * 更新或者新增仓库信息
     *
     * @param warehouseInfo
     * @return
     */
    @Override
    public boolean MySaveOrUpdate(WarehouseInfo warehouseInfo) {

        if (warehouseInfo.getId() == null || warehouseInfo.getId() == 0) {
            //新增
            return this.save(warehouseInfo);
        } else {
            //更新
            WarehouseInfo warehouseInfo1 = getByWarehouseLocation(warehouseInfo.getWarehouseLocation());
            if (warehouseInfo1 == null) {//修改的位置不能是已经有的位置
                return this.updateById(warehouseInfo);
            }
            return false;
        }
    }

    /**
     * 删除仓库信息，对应仓库位置没有经理也没有普通员工也没有存放商品才可以删除
     *
     * @param warehouseInfo
     * @return
     */
    @Override
    public boolean deleteWarehouseInfo(WarehouseInfo warehouseInfo) {

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>().eq(User::getWarehouseLocation,
                warehouseInfo.getWarehouseLocation());
        List<User> userList = userService.getBaseMapper().selectList(lambdaQueryWrapper);
        LambdaQueryWrapper<Goods> goodsLambdaQueryWrapper = new LambdaQueryWrapper<Goods>().eq(Goods::getStorage,
                warehouseInfo.getId());
        List<Goods> goodsList = goodsService.getBaseMapper().selectList(goodsLambdaQueryWrapper);
        if (userList.size() == 0 && warehouseInfo.getManager().equals("暂无经理") && goodsList.size() == 0) {
            return this.removeById(warehouseInfo);
        }
        return false;
    }

    @Override
    public boolean checkStorage(Integer storage) {

        return this.baseMapper.selectById(storage) != null;
    }

    @Override
    public PageUtils queryByWarehouseLocation(String warehouseLocation) {

        LambdaQueryWrapper<WarehouseInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(WarehouseInfo::getWarehouseLocation, warehouseLocation);
        List<WarehouseInfo> warehouseInfoList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(warehouseInfoList, warehouseInfoList.size());
    }
}
