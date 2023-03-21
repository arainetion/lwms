package com.hy.lwmsbackend.utils;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 */
@Setter
@Getter
@ApiModel("分页工具类")
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private int totalCount;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currPage;

    /**
     * 排序 在字段名后加“:asc或:desc”指定升序（降序），多个字段使用逗号分隔，省略排序默认使用升序
     */
    private String order;

    /**
     * 列表数据
     */
    private List<?> list;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {


        int fromIndex = (currPage - 1) * pageSize;
        int toIndex = fromIndex + pageSize;
        if (toIndex > totalCount) {
            toIndex = totalCount;
        }

        this.list = list.subList(fromIndex, toIndex);
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

}
