package cn.edu.seu.historycontest.security;

import cn.edu.seu.historycontest.config.SecurityConfig;
import cn.edu.seu.historycontest.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String sid;
    private String cardId;
    private String name;
    private String password;
    private Integer department;
    private String role;
    private String status;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal ofUser(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        authorities.add(new SimpleGrantedAuthority(user.getStatus()));
        return new UserPrincipal(user.getId(), user.getSid(), user.getCardId(), user.getName(), user.getPassword(),
                user.getDepartment(), user.getRole(), user.getStatus(), authorities);
    }

    public User toUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        if (StringUtils.isEmpty(password))
            return SecurityConfig.bCryptPasswordEncoder.encode(cardId);

        return password;
    }

    @Override
    public String getUsername() {
        return sid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
