package com.yu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.model.dto.nav.NavQueryRequest;
import com.yu.model.entity.Nav;
import com.yu.model.vo.NavVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 导航服务
 *
 */
public interface NavService extends IService<Nav> {

    /**
     * 校验数据
     *
     * @param nav
     * @param add 对创建的数据进行校验
     */
    void validNav(Nav nav, boolean add);

    /**
     * 获取查询条件
     *
     * @param navQueryRequest
     * @return
     */
    QueryWrapper<Nav> getQueryWrapper(NavQueryRequest navQueryRequest);
    
    /**
     * 获取导航封装
     *
     * @param nav
     * @param request
     * @return
     */
    NavVO getNavVO(Nav nav, HttpServletRequest request);

    /**
     * 分页获取导航封装
     *
     * @param navPage
     * @param request
     * @return
     */
    Page<NavVO> getNavVOPage(Page<Nav> navPage, HttpServletRequest request);
}
