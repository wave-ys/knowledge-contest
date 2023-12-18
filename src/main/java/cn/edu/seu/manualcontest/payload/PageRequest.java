package cn.edu.seu.manualcontest.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class PageRequest {
    @NotNull(message = "页码不能为空")
    @Positive(message = "页码必须为正数")
    private Integer pageIndex;

    @NotNull(message = "每页条数不能为空")
    @Positive(message = "每页条数必须为正数")
    private Integer pageSize;

    private String queryType;
    private String queryValue;
}
