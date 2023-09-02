package com.profi_shop.model;

import com.profi_shop.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.sql.Date;

@Data
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    @Enumerated
    private Role role;
    private boolean active;

    private Date createdDate;

    @PrePersist
    private void onCreate(){
        this.createdDate = Date.valueOf(LocalDate.now());
    }

    public User() {
    }

    public User(Long id,
                String username,
                String password,
                boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
    }
    public User(Long id,
                String username,
                String password,
                boolean active,
                Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword(){
        return password;
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
        return isActive();
    }
}
