package com.yu.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dtk.main.ApiClient;
import com.yu.common.BaseResponse;
import com.yu.common.ResultUtils;
import com.yu.model.dto.store.StoreQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeMap;

/**
 * 商城接口
 */
@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {

    @Value("${dataoke.appKey}")
    private String appKey;

    @Value("${dataoke.appSecret}")
    private String appSecret;

    @PostMapping("/data")
    public BaseResponse<JSONObject> getData(@RequestBody StoreQueryRequest storeQueryRequest){


        String url = "https://openapi.dataoke.com/api/goods/explosive-goods-list";
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.0.0");
        paraMap.put("appKey", appKey);

        paraMap.put("pageSize", storeQueryRequest.getPageSize());
        paraMap.put("pageId", storeQueryRequest.getPageId());
        String data = ApiClient.sendReq(url, appSecret, paraMap);

        JSONObject json = JSONUtil.parseObj(data);
        String data1 = json.getStr("data");

        return ResultUtils.success(json);
    }
}
