package cn.edu.seu.historycontest.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {

    @NotBlank(message = "学号不能为空")
    private String sid;
    @NotBlank(message = "一卡通号不能为空")
    private String cardId;
    @NotBlank(message = "姓名不能为空")
    private String name;

}
