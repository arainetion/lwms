package com.hy.lwmsbackend.sys.mapper;

import com.hy.lwmsbackend.sys.pojo.GoodsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

    @Select("SELECT gt.`name` FROM lwms.goods_type gt, lwms.goods g WHERE g.type = #{id}")
    List<String> selectOneToDelete(Integer id);
}
