package com.yu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.model.entity.TArticleCategoryRel;
import com.yu.mapper.TArticleCategoryRelMapper;
import com.yu.service.TArticleCategoryRelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author joe
* @description 针对表【t_article_category_rel(文章所属分类映射表)】的数据库操作Service实现
* @createDate 2024-04-08 15:12:34
*/
@Service
public class TArticleCategoryRelServiceImpl extends ServiceImpl<TArticleCategoryRelMapper, TArticleCategoryRel>
    implements TArticleCategoryRelService{

    @Resource
    private TArticleCategoryRelMapper tArticleCategoryRelMapper;

    @Override
    public boolean removeByArticleId(long id) {
        return tArticleCategoryRelMapper.removeByArticleId(id);
    }
}




