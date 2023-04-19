package com.hy.lwmsbackend.sys.controller;


import com.hy.lwmsbackend.sys.pojo.Goods;
import com.hy.lwmsbackend.sys.service.IGoodsService;
import com.hy.lwmsbackend.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.parameters.P;
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
@RequestMapping("/sys/goods")
@Api(tags = "商品类相关操作", value = "商品信息")
public class GoodsController {

    @Resource
    private IGoodsService goodsService;

    @ApiOperation("根据商品名称模糊查询商品信息")
    @GetMapping("/list")
    public PageUtils listByName(@RequestParam("name") String name,
                          @RequestParam("pageIndex") int pageIndex,
                          @RequestParam("pageSize") int pageSize) {

        return goodsService.queryByName(name, pageIndex, pageSize);
    }

    @ApiOperation("根据商品ID查询商品信息")
    @GetMapping("/listById")
    public PageUtils listById(@RequestParam("goods") String id) {

        return goodsService.queryById(id);
    }

    @ApiOperation("根据条件查询商品信息")
    @GetMapping("/listBy")
    public PageUtils listBy(@RequestParam("name") String name,
                            @RequestParam("storage") String storage,
                            @RequestParam("goodsType") String goodsType,
                            @RequestParam("pageIndex") int pageIndex,
                            @RequestParam("pageSize") int pageSize) {

        return goodsService.queryBy(name, storage, goodsType, pageIndex, pageSize);
    }

    @ApiOperation("新增或更新商品")
    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody Goods goods) {

        return goodsService.MySaveOrUpdate(goods);
    }

    @ApiOperation("删除商品")
    @PostMapping("/deleteGoods")
    public boolean deleteGoods(@RequestBody Goods goods) {

        return goodsService.deleteGoods(goods);
    }

}
