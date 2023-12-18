package cn.edu.seu.manualcontest.service.impl;

import cn.edu.seu.manualcontest.payload.AuthResponse;
import cn.edu.seu.manualcontest.security.JwtTokenProvider;
import cn.edu.seu.manualcontest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public String login(String ticket) {
        String cardId = null;

        try {
            String url = "https://xgbxscwx.seu.edu.cn/cas-we-can/serviceValidate?ticket=" + ticket
                    + "&service=" + "https://xgbxscwx.seu.edu.cn/manual/"
                    + "&json=1";
            AuthResponse response = restTemplate.getForObject(url,AuthResponse.class);
            assert response != null;
            cardId = response.getCas_info().getCardnum();
        } catch (Exception e) {
            String url = "https://newids.seu.edu.cn/authserver/serviceValidate?service=" + ("https://xgbxscwx.seu.edu.cn/manual/")
                    + "&ticket=" + ticket;
            String response = restTemplate.getForObject(url, String.class);
            if (response == null)
                throw new BadCredentialsException("登录错误，请重试");

            Matcher matcher = Pattern.compile("<cas:uid>(.*)</cas:uid>").matcher(response);
            if (!matcher.find())
                throw new BadCredentialsException("登录错误，请重试");
            cardId = matcher.group(1);
        }

        return login(cardId, cardId);}


    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }
}