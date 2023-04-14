package com.hy.lwmsbackend.sys.service;

import com.hy.lwmsbackend.sys.pojo.WarehouseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hy.lwmsbackend.utils.PageUtils;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2023-03-17
 */
public interface IWarehouseInfoService extends IService<WarehouseInfo> {

    String checkWarehouseLocation(String warehouseLocation);

    List<WarehouseInfo> getByManger(String manger);

    WarehouseInfo getByWarehouseLocation(String warehouseLocation);

    PageUtils queryByWarehouseLocation(String warehouseLocation, int pageIndex, int pageSize,boolean flag);

    boolean MySaveOrUpdate(WarehouseInfo warehouseInfo);

    boolean deleteWarehouseInfo(WarehouseInfo warehouseInfo);

    boolean checkStorage(Integer storage);

    PageUtils queryByWarehouseLocation(String warehouseLocation);
}
