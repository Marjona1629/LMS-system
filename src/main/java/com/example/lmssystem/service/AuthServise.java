package com.example.lmssystem.service;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.config.security.JwtProvider;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.BranchRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServise {
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<ResponseData> createUser(CreateUserDTO createUserDTO) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       User user =User.builder()
               .branches(List.of(branchRepository.findById(createUserDTO.branchId()).orElseThrow()))
               .deleted(false)
               .gender(createUserDTO.gender())
               .firstName(createUserDTO.firstName())
               .lastName(createUserDTO.lastName())
               .role(List.of(roleRepository.findByName("USER").orElseThrow()))
               .locale("en")
               .birthDate(sdf.parse(createUserDTO.birthDate()))
               .canLogin(true)
               .phoneNumber(createUserDTO.phoneNumber())
               .build();

       User save=setUserNameAndPassword(user);
        return ResponseEntity.status(200).body(
               ResponseData.builder()
                       .data(save)
                       .message(Utils.getMessage("userSaved"))
                       .success(true)
                       .build()
       );
    }
    private User setUserNameAndPassword(User user1){
        String firstName = user1.getFirstName();
        String lastName = user1.getLastName();
        User user = userRepository.save(user1);
        user.setPassword(firstName+"_"+lastName+"_"+user.getId());
        user.setUsername(firstName+"_"+lastName+"_"+user.getId());
        user.setPasswordSize(user.getPassword().length());
        userRepository.set(user.getPassword(),user.getUsername(),user.getPasswordSize(),user.getId());
        return user;
    }

    public ResponseEntity<?> signIn(String username, String password) {
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
        if(!(customUserDetails.getPassword()!=null || customUserDetails.getPassword().equals(password))){
            return ResponseEntity.status(400).body(ResponseData.builder()
                    .success(false)
                    .message("loginFailed")
                    .data(null)
                    .build()
            );
        }
        String token = jwtProvider.generateToken(customUserDetails);
        System.out.println(token);
        return ResponseEntity.status(200).body(ResponseData.builder()
                .success(true)
                .message("loginSuccess")
                .data(token)
                .build()
        );
    }
}