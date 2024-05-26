package com.yu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章所属分类映射表
 * @TableName t_article_category_rel
 */
@TableName(value ="t_article_category_rel")
@Data
public class TArticleCategoryRel implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    private Long article_id;

    /**
     * 分类id
     */
    private Long category_id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}