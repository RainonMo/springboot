package com.yu.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.common.ErrorCode;
import com.yu.constant.CommonConstant;
import com.yu.exception.ThrowUtils;
import com.yu.mapper.NavMapper;
import com.yu.model.dto.nav.NavQueryRequest;
import com.yu.model.entity.Nav;
import com.yu.model.entity.User;
import com.yu.model.vo.NavVO;
import com.yu.model.vo.UserVO;
import com.yu.service.NavService;
import com.yu.service.UserService;
import com.yu.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 导航服务实现
 *
 */
@Service
@Slf4j
public class NavServiceImpl extends ServiceImpl<NavMapper, Nav> implements NavService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param nav
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validNav(Nav nav, boolean add) {
        ThrowUtils.throwIf(nav == null, ErrorCode.PARAMS_ERROR);
        String name = nav.getName();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(name)) {
            ThrowUtils.throwIf(name.length() > 128, ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    /**
     * 获取查询条件
     *
     * @param navQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Nav> getQueryWrapper(NavQueryRequest navQueryRequest) {
        QueryWrapper<Nav> queryWrapper = new QueryWrapper<>();
        if (navQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = navQueryRequest.getId();
//        Long notId = navQueryRequest.getNotId();
        String name = navQueryRequest.getName();
        String category = navQueryRequest.getCategory();
        String url = navQueryRequest.getUrl();
        String sortField = navQueryRequest.getSortField();
        String sortOrder = navQueryRequest.getSortOrder();
        Long userId = navQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        // 精确查询
//        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取导航封装
     *
     * @param nav
     * @param request
     * @return
     */
    @Override
    public NavVO getNavVO(Nav nav, HttpServletRequest request) {
        // 对象转封装类
        NavVO navVO = NavVO.objToVo(nav);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = nav.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        navVO.setUser(userVO);
        // endregion

        return navVO;
    }

    /**
     * 分页获取导航封装
     *
     * @param navPage
     * @param request
     * @return
     */
    @Override
    public Page<NavVO> getNavVOPage(Page<Nav> navPage, HttpServletRequest request) {
        List<Nav> navList = navPage.getRecords();
        Page<NavVO> navVOPage = new Page<>(navPage.getCurrent(), navPage.getSize(), navPage.getTotal());
        if (CollUtil.isEmpty(navList)) {
            return navVOPage;
        }
        // 对象列表 => 封装对象列表
        List<NavVO> navVOList = navList.stream().map(nav -> {
            return NavVO.objToVo(nav);
            }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = navList.stream().map(Nav::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        navVOList.forEach(navVO -> {
            Long userId = navVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            navVO.setUser(userService.getUserVO(user));
        });
        // endregion

        navVOPage.setRecords(navVOList);
        return navVOPage;
    }

}
