package com.hy.lwmsbackend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.lwmsbackend.sys.pojo.WarehouseInfo;
import com.hy.lwmsbackend.sys.mapper.WarehouseInfoMapper;
import com.hy.lwmsbackend.sys.service.IWarehouseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2023-03-17
 */
@Service
public class WarehouseInfoServiceImpl extends ServiceImpl<WarehouseInfoMapper, WarehouseInfo> implements IWarehouseInfoService {


    /**
     * 查询仓库经理，并返回经理账号
     * @param warehouseLocation
     * @return
     */
    @Override
    public String checkWarehouseLocation(String warehouseLocation) {

        QueryWrapper<WarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(warehouseLocation),"warehouse_location",warehouseLocation);
        WarehouseInfo warehouseInfo = this.getOne(queryWrapper);
        return warehouseInfo == null ? "fail" : warehouseInfo.getManager();
    }

    /**
     * 通过经理返回一条仓库记录
     * @param manger
     * @return
     */
    @Override
    public List<WarehouseInfo> getByManger(String manger) {

        QueryWrapper<WarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(manger),"manager",manger);
        return this.getBaseMapper().selectList(queryWrapper);
    }

    /**
     * 通过仓库位置返回一条仓库记录了
     * @param warehouseLocation
     * @return
     */
    @Override
    public WarehouseInfo getByWarehouseLocation(String warehouseLocation) {

        QueryWrapper<WarehouseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(warehouseLocation),"warehouse_location",warehouseLocation);

        return this.getOne(queryWrapper);
    }
}
