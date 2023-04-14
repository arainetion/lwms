package com.hy.lwmsbackend.sys.controller;


import com.hy.lwmsbackend.sys.pojo.GoodsType;
import com.hy.lwmsbackend.sys.service.IGoodsTypeService;
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
 * @since 2023-03-13
 */
@RestController
@RequestMapping("/sys/goodsType")
@Api(tags = "商品类型相关操作", value = "商品类型信息")
public class GoodsTypeController {

    @Resource
    private IGoodsTypeService goodsTypeService;

    @ApiOperation("根据分类名称查询商品分类信息")
    @GetMapping("/list")
    public PageUtils list(@RequestParam("name") String name,
                          @RequestParam("pageIndex") int pageIndex,
                          @RequestParam("pageSize") int pageSize) {

        return goodsTypeService.queryByName(name, pageIndex, pageSize);
    }

    @ApiOperation("检查分类名称是否已存在")
    @GetMapping("/checkName")
    public boolean checkName(@RequestParam("name") String name) {

        return goodsTypeService.checkName(name);
    }

    @ApiOperation("新增或更新商品分类")
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody GoodsType goodsType) {

        return goodsTypeService.MySaveOrUpdate(goodsType);
    }

    @ApiOperation("删除商品分类信息")
    @PostMapping("/deleteGoodsType")
    public boolean deleteGoodsType(@RequestBody GoodsType goodsType){

        return goodsTypeService.deleteGoodsType(goodsType);
    }

    @ApiOperation("检查商品分类是否合法")
    @GetMapping("/checkGoodsType")
    public boolean checkStorage(@RequestParam("goodsType") Integer goodsType){

        return goodsTypeService.checkStorage(goodsType);
    }
}
