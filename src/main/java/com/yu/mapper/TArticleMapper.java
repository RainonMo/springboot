package com.yu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.model.dto.tarticle.TArticleQueryRequest;
import com.yu.model.entity.TArticle;
import com.yu.model.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author joe
* @description 针对表【t_article(文章表)】的数据库操作Mapper
* @createDate 2024-04-08 13:54:26
* @Entity com.yu.model.entity.TArticle
*/
public interface TArticleMapper extends BaseMapper<TArticle> {

    // 获取文章数据
    List<ArticleVO> listTArticle(@Param("title")String title, @Param("current")int current,@Param("pageSize") int pageSize);
}




