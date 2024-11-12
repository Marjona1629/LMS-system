package com.example.lmssystem.config.security;

import com.example.lmssystem.entity.Branch;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordSize;
    private String imageUrl;
    private Long roleId;
    private Branch branch;
    private Boolean canLogin;
    private String gender;
    private Date birthDate;
    private String locale;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}