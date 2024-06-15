package com.yu.model.dto.store;

import lombok.Data;

import java.io.Serializable;

@Data
public class StoreQueryRequest implements Serializable {
    /**
     * 分页
     */
    private String pageId;
    /**
     * 每页数目
     */
    private String pageSize;
    /**
     * 价格区间
     */
    private String priceCid;
    /**
     * 分类
     */
    private String cids;
}
