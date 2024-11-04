package com.yu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.model.dto.tarticle.TArticleQueryRequest;
import com.yu.model.entity.TArticle;
import com.yu.model.vo.ArticleVO;

import java.util.List;

/**
* @author joe
* @description 针对表【t_article(文章表)】的数据库操作Service
* @createDate 2024-04-08 13:54:26
*/
public interface TArticleService extends IService<TArticle> {

    /**
     * 获取文章数据
     * @param tArticleQueryRequest
     * @return
     */
    List<ArticleVO> listTArticle(TArticleQueryRequest tArticleQueryRequest);

    /**
     * 获取文章数量
     * @param tArticleQueryRequest
     * @return
     */
    Long getTArticleVOCount(TArticleQueryRequest tArticleQueryRequest);
}
