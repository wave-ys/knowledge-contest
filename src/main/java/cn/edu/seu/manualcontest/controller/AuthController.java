package cn.edu.seu.manualcontest.controller;

import cn.edu.seu.manualcontest.payload.LoginRequest;
import cn.edu.seu.manualcontest.payload.LoginResponse;
import cn.edu.seu.manualcontest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return new LoginResponse(authService.login(loginRequest.getTicket()));
    }
}