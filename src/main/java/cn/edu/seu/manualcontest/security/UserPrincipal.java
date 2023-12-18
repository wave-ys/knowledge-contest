package cn.edu.seu.manualcontest.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserPrincipal extends UserDetails {

    String getRole();
    void setPassword(String password);

}
