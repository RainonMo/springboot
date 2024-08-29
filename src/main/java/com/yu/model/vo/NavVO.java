package com.yu.model.vo;

import com.yu.model.vo.UserVO;
import cn.hutool.json.JSONUtil;
import com.yu.model.entity.Nav;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 导航视图
 *
 */
@Data
public class NavVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String url;

    /**
     * 图像
     */
    private String icon;

    /**
     * 简洁
     */
    private String profile;

    /**
     * 分类
     */
    private String category;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param navVO
     * @return
     */
    public static Nav voToObj(NavVO navVO) {
        if (navVO == null) {
            return null;
        }
        Nav nav = new Nav();
        BeanUtils.copyProperties(navVO, nav);
        return nav;
    }

    /**
     * 对象转封装类
     *
     * @param nav
     * @return
     */
    public static NavVO objToVo(Nav nav) {
        if (nav == null) {
            return null;
        }
        NavVO navVO = new NavVO();
        BeanUtils.copyProperties(nav, navVO);
        return navVO;
    }
}
