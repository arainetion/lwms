package com.hy.lwmsbackend.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author hy
 * @since 2023-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "WarehouseInfo对象", description = "仓库信息")
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "仓库位置")
    @TableField("warehouse_location")
    private String warehouseLocation;

    @ApiModelProperty(value = "仓库名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    /*
        @TableField(fill = FieldFill.INSERT_UPDATE)
        更新或插入null值时，列入语句
            ==>  Preparing: UPDATE warehouse_info SET warehouse_location=?, name=?, remark=?, manager=? WHERE id=?
            ==> Parameters: 上海(String), 上海仓库(String), 上海各区仓库管理(String), null, 1(Integer)
     */
    @ApiModelProperty(value = "经理")
    @TableField(value = "manager")
    private String manager;


}
