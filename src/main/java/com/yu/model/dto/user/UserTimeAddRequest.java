package com.yu.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTimeAddRequest  implements Serializable {

    /**
     * cron
     */
    private String cron;

    /**
     * 产品id
     */
    private Long productId;

    private static final long serialVersionUID = 1L;
}
