package com.hy.lwmsbackend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hy.lwmsbackend.sys.pojo.Goods;
import com.hy.lwmsbackend.sys.mapper.GoodsMapper;
import com.hy.lwmsbackend.sys.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.lwmsbackend.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Override
    public PageUtils queryByName(String name, int pageIndex, int pageSize) {

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<Goods>().like(Goods::getName, name);
        List<Goods> goodsList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(goodsList, goodsList.size(), pageSize, pageIndex);
    }

    @Override
    public boolean MySaveOrUpdate(Goods goods) {

        return this.saveOrUpdate(goods);
    }

    @Override
    public boolean deleteGoods(Goods goods) {

        if (goods.getCount() == 0) {
            return this.removeById(goods);
        } else {
            return false;
        }
    }

    @Override
    public PageUtils queryBy(String name, String storage, String goodsType, int pageIndex, int pageSize) {

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && "null".equals(name)) {
            lambdaQueryWrapper.like(Goods::getName, name);
        }
        if (StringUtils.isNotBlank(storage) && !("null".equals(storage))) {
            lambdaQueryWrapper.eq(Goods::getStorage, storage);
        }
        if (StringUtils.isNotBlank(goodsType) && !("null".equals(goodsType))) {
            lambdaQueryWrapper.eq(Goods::getGoodsType, goodsType);
        }
        List<Goods> goodsList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(goodsList, goodsList.size(), pageSize, pageIndex);
    }

    @Override
    public PageUtils queryById(String id) {

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(id) && "null".equals(id)){
            lambdaQueryWrapper.eq(Goods::getId, Integer.valueOf(id));
        }
        List<Goods> goodsList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(goodsList, goodsList.size(),true);
    }
}
