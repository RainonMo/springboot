package com.yu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.model.entity.Chart;
import com.yu.service.ChartService;
import com.yu.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author joe
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




