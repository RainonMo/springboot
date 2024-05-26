package com.yu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yu.common.BaseResponse;
import com.yu.common.ResultUtils;
import com.yu.model.entity.TCategory;
import com.yu.service.TCategoryService;
import com.yu.mapper.TCategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author joe
* @description 针对表【t_category(文章分类表)】的数据库操作Service实现
* @createDate 2024-04-07 17:57:33
*/
@Service
public class TCategoryServiceImpl extends ServiceImpl<TCategoryMapper, TCategory>
    implements TCategoryService{

}




