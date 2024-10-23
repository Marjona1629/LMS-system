package com.example.lmssystem.servise;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> authUserOptional = repository.findByUserna   meAndDeletedFalse(username);
        if (authUserOptional.isEmpty()) {
            return null;
        }

        AuthUser authUser = authUserOptional.get();
        return CustomUserDetails.builder()
                .username(authUser.getUsername())
                .id(authUser.getId())
                .canLogin(authUser.getCanLogin())
                .password(authUser.getPassword())
                .authorities(getAuthorities(authUser.getRole()))
                .locale(authUser.getLocale())
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                .collect(Collectors.toSet());

    }
}
