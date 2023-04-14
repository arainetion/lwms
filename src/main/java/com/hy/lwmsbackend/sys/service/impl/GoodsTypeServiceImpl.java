package com.hy.lwmsbackend.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hy.lwmsbackend.sys.mapper.GoodsMapper;
import com.hy.lwmsbackend.sys.pojo.GoodsType;
import com.hy.lwmsbackend.sys.mapper.GoodsTypeMapper;
import com.hy.lwmsbackend.sys.service.IGoodsTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hy.lwmsbackend.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {

    @Resource
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public PageUtils queryByName(String name, int pageIndex, int pageSize) {

        LambdaQueryWrapper<GoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<GoodsType>().like(GoodsType::getName, name);
        List<GoodsType> goodsTypeList = this.baseMapper.selectList(lambdaQueryWrapper);
        return new PageUtils(goodsTypeList, goodsTypeList.size(), pageSize, pageIndex);
    }

    @Override
    public boolean checkName(String name) {

        LambdaQueryWrapper<GoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<GoodsType>().eq(GoodsType::getName, name);
        List<GoodsType> goodsTypeList = this.baseMapper.selectList(lambdaQueryWrapper);
        return goodsTypeList.size() > 0;
    }

    @Override
    public boolean MySaveOrUpdate(GoodsType goodsType) {

        return this.saveOrUpdate(goodsType);
    }

    /**
     * 若分类没有被商品表使用，才可以删除该条记录
     * @param goodsType
     * @return
     */
    @Override
    public boolean deleteGoodsType(GoodsType goodsType) {
        List<String> name = goodsTypeMapper.selectOneToDelete(goodsType.getId());
        if (name.size() < 1){
            return this.removeById(goodsType);
        }
        return false;
    }

    @Override
    public boolean checkStorage(Integer goodsType) {

        return this.baseMapper.selectById(goodsType) != null;
    }
}
