package com.hy.lwmsbackend.sys.service;

import com.hy.lwmsbackend.sys.pojo.WarehouseInfo;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
