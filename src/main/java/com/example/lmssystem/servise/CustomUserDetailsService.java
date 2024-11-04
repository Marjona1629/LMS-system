package com.example.lmssystem.servise;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> authUserOptional = repository.findByUsernameAndDeletedFalse(username);
        if (authUserOptional.isEmpty()) {

            return null;
        }
        List<Long> branches=new ArrayList<>();
        User authUser = authUserOptional.get();
        for (Branch branch : authUser.getBranches()) {
            branches.add(branch.getId());
        }
        CustomUserDetails build = CustomUserDetails.builder()
                .id(authUser.getId())
                .username(authUser.getUsername())
                .password(authUser.getPassword())
                .firstName(authUser.getFirstName())
                .lastName(authUser.getLastName())
                .canLogin(authUser.getCanLogin())
                .birthDate(authUser.getBirthDate())
                .branches(branches)
                .imageUrl(authUser.getImageUrl())
                .phoneNumber(authUser.getPhoneNumber())
                .gender(authUser.getGender().toString())
                .locale(authUser.getLocale())
                .passwordSize(authUser.getPasswordSize())
                .build();
        return build;
    }


}
