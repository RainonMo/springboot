package ${packageName}.model.dto.${dataKey};

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑${dataName}请求
 *
 */
@Data
public class ${upperDataKey}EditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    private static final long serialVersionUID = 1L;
}