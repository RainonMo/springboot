package com.yu.controller;

import com.yu.common.BaseResponse;
import com.yu.common.ErrorCode;
import com.yu.common.ResultUtils;
import com.yu.exception.ThrowUtils;
import com.yu.model.dto.message.MessageAddRequest;
import com.yu.model.entity.Message;
import com.yu.model.entity.User;
import com.yu.service.MessageService;
import com.yu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    @PostMapping("/send")
    public BaseResponse<Long> sendByUser(@RequestBody MessageAddRequest messageAddRequest, HttpServletRequest request){
        ThrowUtils.throwIf(messageAddRequest==null, ErrorCode.PARAMS_ERROR);
        Message message = new Message();
        BeanUtils.copyProperties(messageAddRequest,message);
        //参数校验 todo
//        messageService.validMessage(message,true);
        User loginUser = userService.getLoginUser(request);
        message.setUserId(loginUser.getId());
        boolean result = messageService.save(message);
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        Long newMessageId = message.getId();
        return ResultUtils.success(newMessageId);
    }


}
