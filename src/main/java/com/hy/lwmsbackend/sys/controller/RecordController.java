package com.hy.lwmsbackend.sys.controller;


import com.hy.lwmsbackend.sys.pojo.Record;
import com.hy.lwmsbackend.sys.service.IGoodsService;
import com.hy.lwmsbackend.sys.service.IGoodsTypeService;
import com.hy.lwmsbackend.sys.service.IRecordService;
import com.hy.lwmsbackend.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/sys/record")
@Api(tags = "操作记录相关操作", value = "操作记录信息")
public class RecordController {

    @Resource
    private IRecordService recordService;

    @ApiOperation("根据分类名称查询操作记录")
    @GetMapping("/list")
    public PageUtils list(@RequestParam("goods") String goods,
                          @RequestParam("pageIndex") int pageIndex,
                          @RequestParam("pageSize") int pageSize) {

        return recordService.queryByGoods(goods, pageIndex, pageSize);
    }

    @ApiOperation("保存操作记录")
    @PostMapping("/saveRecord")
    public boolean saveRecord(@RequestBody Record record){

        System.out.println(record.toString());
        return recordService.saveRecord(record);
    }
}
