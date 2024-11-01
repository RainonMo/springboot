package com.yu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.constant.CommonConstant;
import com.yu.mapper.TCategoryMapper;
import com.yu.model.dto.tcategory.TCategoryQueryRequest;
import com.yu.model.entity.TCategory;
import com.yu.service.TCategoryService;
import com.yu.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author joe
* @description 针对表【t_category(文章分类表)】的数据库操作Service实现
* @createDate 2024-04-07 17:57:33
*/
@Service
public class TCategoryServiceImpl extends ServiceImpl<TCategoryMapper, TCategory>
    implements TCategoryService{

    @Override
    public QueryWrapper<TCategory> getQueryWrapper(TCategoryQueryRequest tCategoryQueryRequest) {
        QueryWrapper<TCategory> queryWrapper = new QueryWrapper<>();
        if (tCategoryQueryRequest == null) {
            return queryWrapper;
        }

        // 取值
        String name = tCategoryQueryRequest.getName();
        String sortField = tCategoryQueryRequest.getSortField();
        String sortOrder = tCategoryQueryRequest.getSortOrder();

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        // 精确查询
//        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return queryWrapper;
    }
}




