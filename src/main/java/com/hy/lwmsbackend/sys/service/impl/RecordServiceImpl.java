package com.hy.lwmsbackend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hy.lwmsbackend.sys.pojo.Goods;
import com.hy.lwmsbackend.sys.pojo.Record;
import com.hy.lwmsbackend.sys.mapper.RecordMapper;
import com.hy.lwmsbackend.sys.service.IGoodsService;
import com.hy.lwmsbackend.sys.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.lwmsbackend.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Resource
    private IGoodsService goodsService;

    @Override
    public PageUtils queryByGoods(String goods, int pageIndex, int pageSize) {

        LambdaQueryWrapper<Record> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(goods) && !("null".equals(goods))) {
            lambdaQueryWrapper.eq(Record::getGoods, Integer.valueOf(goods));
        }
        List<Record> recordList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(recordList, recordList.size(), pageSize, pageIndex);
    }

    @Override
    public boolean saveRecord(Record record) {

        LocalDateTime time = LocalDateTime.now();
        record.setCreateTime(time);

        //出库或者入库前的数量
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getId,record.getGoods());
        Goods goods = goodsService.getBaseMapper().selectOne(queryWrapper);
        Integer preCount = goods.getCount();
        if (record.getCount() > 0){//入库
            goods.setCount(preCount + record.getCount());
            boolean update = goodsService.updateById(goods);
            return this.save(record) && update;
        }
        if (record.getCount() < 0){//出库
            goods.setCount(preCount + record.getCount());
            boolean update = goodsService.updateById(goods);
            return this.save(record) && update;
        }
        return false;
    }
}
