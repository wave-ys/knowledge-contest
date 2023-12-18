package cn.edu.seu.historycontest.service.impl;

import cn.edu.seu.historycontest.entity.User;
import cn.edu.seu.historycontest.security.JwtTokenProvider;
import cn.edu.seu.historycontest.service.AuthService;
import cn.edu.seu.historycontest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Override
    public String login(String sid, String password, String code) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(sid, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public void register(String sid, String cardId, String name) {
        User user = new User();
        user.setSid(sid);
        user.setCardId(cardId);
        user.setName(name);
        userService.insertStudent(user);
    }
}
