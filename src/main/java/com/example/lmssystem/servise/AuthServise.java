package com.example.lmssystem.servise;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.config.security.JwtProvider;
import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.enums.Gender;
import com.example.lmssystem.repository.BranchRepository;
import com.example.lmssystem.repository.FinanceRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.trnasfer.ResponseData;
import com.example.lmssystem.trnasfer.auth.CreateUserDTO;
import com.example.lmssystem.trnasfer.auth.ProfileDTO;
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
    private final FinanceRepository financeRepository;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<ResponseData> createUser(CreateUserDTO createUserDTO) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
       User user =User.builder()
               .branches(List.of(branchRepository.findById(createUserDTO.branchId()).orElseThrow()))
               .deleted(false)
               .gender(Gender.valueOf(createUserDTO.gender().toString().toUpperCase()))
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
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < user.getPassword().length(); i++) {
            sb.append("*");
        }
        user.setPasswordSize(sb.toString());
        userRepository.save(user);
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

    public ResponseEntity<?> profile() {
        CustomUserDetails customUserDetails = Utils.sessionUser();
        StringBuilder stringBuilder=new StringBuilder();
        Branch branch1=new Branch();
        for (Long branch : customUserDetails.getBranches()) {
            try {
               branch1= branchRepository.findById(branch).orElseThrow();
            }catch (Exception e){}
            stringBuilder.append(branch1.getName()).append("\n");

        }
        ProfileDTO profileDTO=new ProfileDTO(
                customUserDetails.getId(),
                customUserDetails.getFirstName(),
                customUserDetails.getLastName(),
                customUserDetails.getPhoneNumber(),
                customUserDetails.getPhoneNumber(),
                stringBuilder.toString(),
                financeRepository.findByUser(customUserDetails.getId()).orElseThrow().getAmount(),
                customUserDetails.getPasswordSize()



        );
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .message(Utils.getMessage("userProfile"))
                        .data(
                                profileDTO
                        ).build()
        );
    }
}
