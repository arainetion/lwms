package com.hy.lwmsbackend.sys.controller;


import com.hy.lwmsbackend.sys.pojo.WarehouseInfo;
import com.hy.lwmsbackend.sys.service.IUserService;
import com.hy.lwmsbackend.sys.service.IWarehouseInfoService;
import com.hy.lwmsbackend.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hy
 * @since 2023-03-17
 */
@RestController
@RequestMapping("/sys/warehouse-info")
@Api(tags = "仓库相关操作", value = "仓库信息")
public class WarehouseInfoController {


    @Resource
    private IWarehouseInfoService warehouseInfoService;


    @ApiOperation("根据仓库位置查询仓库")
    @GetMapping("/list")
    public PageUtils list(@RequestParam("warehouseLocation") String warehouseLocation, @RequestParam("pageIndex") int pageIndex,
                          @RequestParam("pageSize") int pageSize, boolean flag){

        return warehouseInfoService.queryByWarehouseLocation(warehouseLocation,pageIndex,pageSize,flag);
    }

    @ApiOperation("根据仓库位置查询仓库，不分页")
    @GetMapping("/listNo")
    public PageUtils list(@RequestParam("warehouseLocation") String warehouseLocation){

        return warehouseInfoService.queryByWarehouseLocation(warehouseLocation);
    }

    @ApiOperation("返回经理账号")
    @GetMapping("/checkWarehouseLocation")
    public String checkWarehouseLocation(@RequestParam("warehouseLocation") String warehouseLocation) {

        return warehouseInfoService.checkWarehouseLocation(warehouseLocation);
    }

    @ApiOperation("新增或者更新仓库")
    @PostMapping("/save")
    public boolean save(@RequestBody WarehouseInfo warehouseInfo){

        return warehouseInfoService.MySaveOrUpdate(warehouseInfo);
    }

    @ApiOperation("删除仓库信息")
    @PostMapping("/deleteWarehouseInfo")
    public boolean deleteWarehouseInfo(@RequestBody WarehouseInfo warehouseInfo){

        return warehouseInfoService.deleteWarehouseInfo(warehouseInfo);
    }

    @ApiOperation("检查商品存储仓库是否合法")
    @GetMapping("/checkStorage")
    public boolean checkStorage(@RequestParam("storage") Integer storage){

       return warehouseInfoService.checkStorage(storage);
    }
}
