package cn.edu.seu.historycontest.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EditStudentRequest {

    private Long id;
    @NotNull(message = "学号不能为空")
    private String sid;
    @NotNull(message = "一卡通号不能为空")
    private String cardId;
    @NotNull(message = "姓名不能为空")
    private String name;

}
