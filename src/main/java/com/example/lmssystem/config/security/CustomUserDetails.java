package com.example.lmssystem.config.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class CustomUserDetails implements UserDetails {
    private String id;
    private String username;
    private String password;
    private Boolean canLogin;
    private Collection<? extends GrantedAuthority> authorities;
    private String locale;
}
