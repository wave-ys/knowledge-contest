package cn.edu.seu.manualcontest.payload;

import lombok.Data;

@Data
public class LoginRequest {

//    @NotBlank(message = "用户名不能为空")
//    private String username;
//
//    @NotBlank(message = "密码不能为空")
//    private String password;

    private String ticket;

}
