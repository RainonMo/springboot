package com.yu.model.dto.nav;

import com.yu.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询导航请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NavQueryRequest extends PageRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}