package com.yu.model.dto.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageAddRequest implements Serializable {
    /**
     * 消息
     */
    private String content;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 时间id
     */
    private Long timeId;

    private static final long serialVersionUID = 1L;
}
