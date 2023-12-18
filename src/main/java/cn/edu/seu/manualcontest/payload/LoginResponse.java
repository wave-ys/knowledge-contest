package cn.edu.seu.manualcontest.payload;

import lombok.Data;

@Data
public class LoginResponse {

    private String tokenType = "Bearer";
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
