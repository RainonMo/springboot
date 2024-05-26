package com.yu.model.dto.tarticle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 */
@Data
public class TArticleUpdateRequest implements Serializable {

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
     * 正文内容
     */
    private String content;

    /**
     * 分类id
     */
    private Long categoryId;


    private static final long serialVersionUID = 1L;

}