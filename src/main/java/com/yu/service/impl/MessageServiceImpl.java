package com.yu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.model.entity.Message;
import com.yu.service.MessageService;
import com.yu.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author joe
* @description 针对表【message(消息表)】的数据库操作Service实现
* @createDate 2024-09-18 16:28:02
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




