package com.yu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.model.entity.TArticleCategoryRel;

/**
* @author joe
* @description 针对表【t_article_category_rel(文章所属分类映射表)】的数据库操作Service
* @createDate 2024-04-08 15:12:34
*/
public interface TArticleCategoryRelService extends IService<TArticleCategoryRel> {

    boolean removeByArticleId(long id);
}
