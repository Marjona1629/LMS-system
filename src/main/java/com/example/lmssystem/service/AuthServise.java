package com.example.lmssystem.service;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.config.security.JwtProvider;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.enums.Gender;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServise {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProvider jwtProvider;
    private final Random random = new Random();

    public ResponseEntity<ResponseData> createUser(CreateUserDTO createUserDTO) throws Exception {
        User user =User.builder()
               .branches(new ArrayList<>())
               .deleted(false)
               .gender(Gender.valueOf(createUserDTO.gender()))
               .firstName(createUserDTO.firstName())
               .lastName(createUserDTO.lastName())
               .role(List.of(roleRepository.findByName("USER").orElseThrow()))
               .locale("en")
               .birthDate(createUserDTO.birthDate())
               .canLogin(true)
               .phoneNumber(createUserDTO.phoneNumber())
               .build();
        setUserNameAndPassword(user);
        return ResponseEntity.status(200).body(
               ResponseData.builder()
                       .data(userRepository.save(user))
                       .message("userSaved")
                       .success(true)
                       .build()
       );
    }
    private void setUserNameAndPassword(User user){
        while (true){
            try {
                user.setUsername(user.getFirstName()+"_"+user.getLastName()+"_"+random.nextInt(10000));
                user.setPassword(passwordEncoder.encode(user.getUsername()));
                user.setPasswordSize(user.getUsername().length());
            }catch (Exception e){
            }
            break;
        }
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

    public ResponseEntity<?> getUser(Long id) {
        return ResponseEntity.status(200).body(ResponseData.builder()
                .success(true)
                .message("Success")
                .data(userRepository.findById(id).orElseThrow())
                .build()
        );
    }

    public ResponseEntity<?> getUsers() {
        return ResponseEntity.status(200).body(ResponseData.builder()
                .success(true)
                .message("Success")
                .data(userRepository.findAll())
                .build()
        );
    }
}