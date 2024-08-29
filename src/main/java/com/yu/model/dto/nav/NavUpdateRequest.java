package com.yu.model.dto.nav;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新导航请求
 *
 */
@Data
public class NavUpdateRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}