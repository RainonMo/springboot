package com.yu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.model.entity.TArticle;
import com.yu.mapper.TArticleMapper;
import com.yu.service.TArticleService;
import org.springframework.stereotype.Service;

/**
* @author joe
* @description 针对表【t_article(文章表)】的数据库操作Service实现
* @createDate 2024-04-08 13:54:26
*/
@Service
public class TArticleServiceImpl extends ServiceImpl<TArticleMapper, TArticle>
    implements TArticleService{

}




