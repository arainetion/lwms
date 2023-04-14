package com.hy.lwmsbackend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hy.lwmsbackend.sys.pojo.Record;
import com.hy.lwmsbackend.sys.mapper.RecordMapper;
import com.hy.lwmsbackend.sys.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.lwmsbackend.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    @Override
    public PageUtils queryByGoods(String goods, int pageIndex, int pageSize) {

        LambdaQueryWrapper<Record> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(goods) && !("null".equals(goods))){
            lambdaQueryWrapper.eq(Record::getGoods, Integer.valueOf(goods));
        }
        List<Record> recordList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(recordList, recordList.size(), pageSize, pageIndex);
    }
}
