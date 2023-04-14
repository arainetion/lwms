package com.hy.lwmsbackend.sys.service;

import com.hy.lwmsbackend.sys.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hy.lwmsbackend.sys.pojo.GoodsType;
import com.hy.lwmsbackend.utils.PageUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
public interface IGoodsService extends IService<Goods> {

    PageUtils queryByName(String name, int pageIndex, int pageSize);

    boolean MySaveOrUpdate(Goods goods);

    boolean deleteGoods(Goods goods);

    PageUtils queryBy(String name, String storage, String goodsType, int pageIndex, int pageSize);

    PageUtils queryById(String id, int pageIndex, int pageSize);
}
