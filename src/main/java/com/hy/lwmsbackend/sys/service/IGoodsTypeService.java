package com.hy.lwmsbackend.sys.service;

import com.hy.lwmsbackend.sys.pojo.GoodsType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hy.lwmsbackend.utils.PageUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
public interface IGoodsTypeService extends IService<GoodsType> {

    PageUtils queryByName(String name, int pageIndex, int pageSize);

    boolean checkName(String name);

    boolean MySaveOrUpdate(GoodsType goodsType);

    boolean deleteGoodsType(GoodsType goodsType);

    boolean checkStorage(Integer goodsType);
}
