package com.yu.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleVO implements Serializable {
    /**
     * 文章id
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章题图
     */
    private String title_image;

    /**
     * 文章描述
     */
    private String description;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 最后一次更新时间
     */
    private Date update_time;


    /**
     * 被阅读次数
     */
    private Object read_num;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 分类id
     */
    private Long categoryId;

    private static final long serialVersionUID = 1L;
}
