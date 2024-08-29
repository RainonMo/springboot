package com.yu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yu.annotation.AuthCheck;
import com.yu.common.BaseResponse;
import com.yu.common.DeleteRequest;
import com.yu.common.ErrorCode;
import com.yu.common.ResultUtils;
import com.yu.constant.UserConstant;
import com.yu.exception.BusinessException;
import com.yu.exception.ThrowUtils;
import com.yu.model.dto.nav.NavAddRequest;
import com.yu.model.dto.nav.NavEditRequest;
import com.yu.model.dto.nav.NavQueryRequest;
import com.yu.model.dto.nav.NavUpdateRequest;
import com.yu.model.entity.Nav;
import com.yu.model.entity.User;
import com.yu.model.vo.NavVO;
import com.yu.service.NavService;
import com.yu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 导航接口
 *
 */
@RestController
@RequestMapping("/nav")
@Slf4j
public class NavController {

    @Resource
    private NavService navService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建导航
     *
     * @param navAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addNav(@RequestBody NavAddRequest navAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(navAddRequest == null, ErrorCode.PARAMS_ERROR);
        Nav nav = new Nav();
        BeanUtils.copyProperties(navAddRequest, nav);
        // 数据校验
        navService.validNav(nav, true);
        User loginUser = userService.getLoginUser(request);
        nav.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = navService.save(nav);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newNavId = nav.getId();
        return ResultUtils.success(newNavId);
    }

    /**
     * 删除导航
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteNav(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Nav oldNav = navService.getById(id);
        ThrowUtils.throwIf(oldNav == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldNav.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = navService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新导航（仅管理员可用）
     *
     * @param navUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateNav(@RequestBody NavUpdateRequest navUpdateRequest) {
        if (navUpdateRequest == null || navUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Nav nav = new Nav();
        BeanUtils.copyProperties(navUpdateRequest, nav);
        // 数据校验
        navService.validNav(nav, false);
        // 判断是否存在
        long id = navUpdateRequest.getId();
        Nav oldNav = navService.getById(id);
        ThrowUtils.throwIf(oldNav == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = navService.updateById(nav);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取导航（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<NavVO> getNavVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Nav nav = navService.getById(id);
        ThrowUtils.throwIf(nav == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(navService.getNavVO(nav, request));
    }

    /**
     * 分页获取导航列表（仅管理员可用）
     *
     * @param navQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Nav>> listNavByPage(@RequestBody NavQueryRequest navQueryRequest) {
        long current = navQueryRequest.getCurrent();
        long size = navQueryRequest.getPageSize();
        // 查询数据库
        Page<Nav> navPage = navService.page(new Page<>(current, size),
                navService.getQueryWrapper(navQueryRequest));
        return ResultUtils.success(navPage);
    }

    /**
     * 分页获取导航列表（封装类）
     *
     * @param navQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<NavVO>> listNavVOByPage(@RequestBody NavQueryRequest navQueryRequest,
                                                               HttpServletRequest request) {
        long current = navQueryRequest.getCurrent();
        long size = navQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Nav> navPage = navService.page(new Page<>(current, size),
                navService.getQueryWrapper(navQueryRequest));
        // 获取封装类
        return ResultUtils.success(navService.getNavVOPage(navPage, request));
    }

    /**
     * 分页获取当前登录用户创建的导航列表
     *
     * @param navQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<NavVO>> listMyNavVOByPage(@RequestBody NavQueryRequest navQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(navQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        navQueryRequest.setUserId(loginUser.getId());
        long current = navQueryRequest.getCurrent();
        long size = navQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Nav> navPage = navService.page(new Page<>(current, size),
                navService.getQueryWrapper(navQueryRequest));
        // 获取封装类
        return ResultUtils.success(navService.getNavVOPage(navPage, request));
    }

    /**
     * 编辑导航（给用户使用）
     *
     * @param navEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editNav(@RequestBody NavEditRequest navEditRequest, HttpServletRequest request) {
        if (navEditRequest == null || navEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Nav nav = new Nav();
        BeanUtils.copyProperties(navEditRequest, nav);
        // 数据校验
        navService.validNav(nav, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = navEditRequest.getId();
        Nav oldNav = navService.getById(id);
        ThrowUtils.throwIf(oldNav == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldNav.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = navService.updateById(nav);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
