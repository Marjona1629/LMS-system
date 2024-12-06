package com.example.lmssystem.service;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.entity.Role;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> authUserOptional = repository.findByUsernameAndDeletedFalse(username);
        User authUser = authUserOptional.orElseThrow();
        List<Long> roles=new ArrayList<>();
        for (Role role : authUser.getRole()) {
            roles.add(role.getId());
        }
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < authUser.getPasswordSize(); i++) {
            password.append("*");
        }
        return CustomUserDetails.builder()
                .id(authUser.getId())
                .username(authUser.getUsername())
                .password(password.toString())
                .firstName(authUser.getFirstName())
                .lastName(authUser.getLastName())
                .canLogin(authUser.getCanLogin())
                .birthDate(authUser.getBirthDate())
                .imageUrl(authUser.getImageUrl())
                .branches(authUser.getBranchess())
                .authorities(authUser.getAuthorities())
                .phoneNumber(authUser.getPhoneNumber())
                .gender(authUser.getGender().toString())
                .locale(authUser.getLocale())

                .build();
    }


}
