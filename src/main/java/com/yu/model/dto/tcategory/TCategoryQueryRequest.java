package com.yu.model.dto.tcategory;


import com.yu.common.PageRequest;
import lombok.Data;
import java.io.Serializable;

/**
 * 查询请求
 *
 */

@Data
public class TCategoryQueryRequest extends PageRequest implements Serializable {


    /**
     * 分类名称
     */
    private String name;

}