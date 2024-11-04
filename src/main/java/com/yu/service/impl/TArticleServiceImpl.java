package com.yu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.model.dto.tarticle.TArticleQueryRequest;
import com.yu.model.entity.TArticle;
import com.yu.mapper.TArticleMapper;
import com.yu.model.vo.ArticleVO;
import com.yu.service.TArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author joe
* @description 针对表【t_article(文章表)】的数据库操作Service实现
* @createDate 2024-04-08 13:54:26
*/
@Service
public class TArticleServiceImpl extends ServiceImpl<TArticleMapper, TArticle>
    implements TArticleService{

    @Resource
    private TArticleMapper tArticleMapper;

    @Override
    public List<ArticleVO> listTArticle(TArticleQueryRequest tArticleQueryRequest) {
        int current = (tArticleQueryRequest.getCurrent()-1)*tArticleQueryRequest.getPageSize();
        int pageSize = tArticleQueryRequest.getPageSize();

        String title = StringUtils.isNotBlank(tArticleQueryRequest.getTitle())?tArticleQueryRequest.getTitle():"";
        return tArticleMapper.listTArticle(title,current,pageSize);
    }

    @Override
    public Long getTArticleVOCount(TArticleQueryRequest tArticleQueryRequest) {
        QueryWrapper<TArticle> queryWrapper = new QueryWrapper<>();
        String title = tArticleQueryRequest.getTitle();
        queryWrapper.like(StringUtils.isNotBlank(title),"title",title);
        return tArticleMapper.selectCount(queryWrapper);
    }
}




