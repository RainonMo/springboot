package com.yu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.model.entity.UserTimeSet;
import com.yu.service.UserTimeSetService;
import com.yu.mapper.UserTimeSetMapper;
import org.springframework.stereotype.Service;

/**
* @author joe
* @description 针对表【user_time_set(用户时间配置表)】的数据库操作Service实现
* @createDate 2024-09-18 16:28:01
*/
@Service
public class UserTimeSetServiceImpl extends ServiceImpl<UserTimeSetMapper, UserTimeSet>
    implements UserTimeSetService{

}




