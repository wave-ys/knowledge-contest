package cn.edu.seu.historycontest.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EditAdminRequest {

    private Long id;
    @NotNull(message = "一卡通号不能为空")
    private String cardId;
    @NotNull(message = "姓名不能为空")
    private String name;

    private Integer department;

}
