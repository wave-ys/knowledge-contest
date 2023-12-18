package cn.edu.seu.manualcontest.security;

import cn.edu.seu.manualcontest.Constants;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest.getServletPath().startsWith("/auth/")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String jwt = getJwtFromRequest(httpServletRequest);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            String role = tokenProvider.getRoleFromJWT(jwt);
            String username = tokenProvider.getUsernameFromJWT(jwt);
            UserDetails userDetails = null;

            if (Constants.ROLE_ADMIN.equals(role))
                userDetails = userDetailsService.loadAdminByCardId(username);
            else if (Constants.ROLE_STUDENT.equals(role))
                userDetails = userDetailsService.loadStudentByStudentId(username);
            else
                throw new JwtException("请重新登录");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails != null ? userDetails.getAuthorities() : null);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
