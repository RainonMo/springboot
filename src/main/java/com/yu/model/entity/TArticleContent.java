package com.yu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章内容表
 * @TableName t_article_content
 */
@TableName(value ="t_article_content")
@Data
public class TArticleContent implements Serializable {
    /**
     * 文章内容id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    private Long article_id;

    /**
     * 正文内容
     */
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}