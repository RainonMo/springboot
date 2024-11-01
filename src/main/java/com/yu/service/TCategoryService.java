package com.yu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.model.dto.tcategory.TCategoryQueryRequest;
import com.yu.model.entity.TCategory;

/**
* @author joe
* @description 针对表【t_category(文章分类表)】的数据库操作Service
* @createDate 2024-04-07 17:57:33
*/
public interface TCategoryService extends IService<TCategory> {

    QueryWrapper<TCategory> getQueryWrapper(TCategoryQueryRequest tCategoryQueryRequest);
}
