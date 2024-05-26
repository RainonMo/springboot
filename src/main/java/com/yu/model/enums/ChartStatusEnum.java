package com.yu.model.enums;

import lombok.Getter;

@Getter
public enum ChartStatusEnum {

    SUCCESS("成功","success"),
    FAIL("失败","fail");

    private final String text;
    private final String value;

    ChartStatusEnum(String text,String value){
        this.text = text;
        this.value = value;
    }

}
