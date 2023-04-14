package com.hy.lwmsbackend.sys.service;

import com.hy.lwmsbackend.sys.pojo.Record;
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
public interface IRecordService extends IService<Record> {

    PageUtils queryByGoods(String goods, int pageIndex, int pageSize);
}
