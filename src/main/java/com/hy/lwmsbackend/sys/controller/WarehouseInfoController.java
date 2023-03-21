package com.hy.lwmsbackend.sys.controller;


import com.hy.lwmsbackend.sys.service.IWarehouseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Api("仓库信息")
public class WarehouseInfoController {


    @Resource
    private IWarehouseInfoService warehouseInfoService;

    @ApiOperation("返回经理账号")
    @GetMapping("/checkWarehouseLocation")
    public String checkWarehouseLocation(@RequestParam("warehouseLocation") String warehouseLocation) {

        return warehouseInfoService.checkWarehouseLocation(warehouseLocation);
    }
}
