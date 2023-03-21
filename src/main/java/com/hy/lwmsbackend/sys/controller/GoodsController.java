package com.hy.lwmsbackend.sys.controller;


import com.hy.lwmsbackend.sys.service.IGoodsService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
@RestController
@RequestMapping("/sys/goods")
@Api(tags = "商品类")
public class GoodsController {

    @Resource
    private IGoodsService goodsService;

}
