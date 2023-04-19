package com.hy.lwmsbackend.sys.controller;


import com.hy.lwmsbackend.sys.pojo.User;
import com.hy.lwmsbackend.sys.service.IUserService;
import com.hy.lwmsbackend.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hy
 * @since 2023-03-13
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "用户相关操作", value = "用户信息")
public class UserController {


    @Resource
    private IUserService userService;

    @GetMapping("/list")
    @ApiOperation("根据用户账号查询用户")
    public PageUtils list(@RequestParam("no") String no, @RequestParam("pageIndex") int pageIndex,
                          @RequestParam("pageSize") int pageSize) {

        return userService.queryByUserNoAndReturnByAuthority(no, pageIndex, pageSize);
    }

    @GetMapping("/listNoPage")
    @ApiOperation("根据用户账号查询用户,不分页")
    public PageUtils listNoPage(@RequestParam("no") String no) {

        return userService.listNoPage(no);
    }

    @GetMapping("/listById")
    @ApiOperation("根据用户Id查询用户")
    public PageUtils listById(String userId) {

        return userService.queryById(userId);

    }

    @GetMapping("/checkNo")
    @ApiOperation("检查账号是否存在")
    public boolean checkNo(@RequestParam("no") String no) {

        List<User> userList = userService.queryByUserNo(no);
        return userList.size() == 1;
    }

    @GetMapping("/listByNo")
    @ApiOperation("根据账号查询name")
    public User listByNo(@RequestParam("no") String no) {

        return userService.listByNo(no);
    }

    @ApiOperation("校验新增用户roleId是否合法")
    @GetMapping("/checkRoleId")
    public Map<String, Object> checkRoleId(@RequestParam("roleId") Integer roleId,
                                           @RequestParam("warehouseLocation") String warehouseLocation,
                                           @RequestParam("no") @ApiParam("新增用户账号") String no) {

        return userService.checkRoleId(roleId, warehouseLocation, no);
    }

    @Transactional
    @ApiOperation("修改用户信息或新增用户")
    @PostMapping("/save")
    public boolean save(@RequestBody User user) {

        return userService.MySaveOrUpdate(user);
    }


    //TODO 假分页改真分页

    @Transactional
    @ApiOperation("根据业务逻辑删除用户")
    @PostMapping("/deleteUser")
    public boolean deleteUser(@RequestBody User user) {

        return userService.removeByIdAndRoleId(user);
    }
}
