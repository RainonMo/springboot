package com.yu.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章分类表
 * @TableName t_category
 */
@TableName(value ="t_category")
@Data
public class TCategory implements Serializable {
    /**
     * 标签id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

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
    @TableLogic
    private Integer is_deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}