package cn.edu.seu.historycontest.controller;

import cn.edu.seu.historycontest.payload.LoginRequest;
import cn.edu.seu.historycontest.payload.LoginResponse;
import cn.edu.seu.historycontest.payload.RegisterRequest;
import cn.edu.seu.historycontest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return new LoginResponse(authService.login(loginRequest.getSid(), loginRequest.getPassword(), loginRequest.getCode()));
    }

    @PostMapping("register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest.getSid(), registerRequest.getCardId(), registerRequest.getName());
    }
}
