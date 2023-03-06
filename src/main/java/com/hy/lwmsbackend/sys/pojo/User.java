package com.hy.lwmsbackend.sys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author arainetion
 * @version 1.0
 * @date 2023/3/4 10:46
 * @Description
 */
@Data
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String  no;
    private String name;
    private String password;
    private int sex;
    private String phone;
    @TableField("isValid")
    private String isValid;
}
