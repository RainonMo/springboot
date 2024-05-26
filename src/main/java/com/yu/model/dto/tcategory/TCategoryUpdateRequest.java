package com.yu.model.dto.tcategory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 */
@Data
public class TCategoryUpdateRequest implements Serializable {

    /**
     * 标签id
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

}