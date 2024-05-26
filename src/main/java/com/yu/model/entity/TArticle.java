package com.yu.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章表
 * @TableName t_article
 */
@TableName(value ="t_article")
@Data
public class TArticle implements Serializable {
    /**
     * 文章id
     */
    @TableId(type = IdType.AUTO)
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
     * 删除标志位：0：未删除 1：已删除
     */
    private Integer is_deleted;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}