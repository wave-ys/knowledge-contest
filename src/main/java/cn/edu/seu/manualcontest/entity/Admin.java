package cn.edu.seu.manualcontest.entity;

import cn.edu.seu.manualcontest.Constants;
import cn.edu.seu.manualcontest.security.UserPrincipal;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-10-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mc_admin")
public class Admin implements Serializable, UserPrincipal {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String cardId;

    private String password;

    private String authority;

    private String department;

    private String name;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @JsonIgnore
    @TableField(exist = false)
    private Collection<SimpleGrantedAuthority> authorities;


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(getRole()));
            authorities.add(new SimpleGrantedAuthority(getAuthority()));
        }

        return authorities;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return cardId;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getRole() {
        return Constants.ROLE_ADMIN;
    }

    public void setRole(String role) {}
}
