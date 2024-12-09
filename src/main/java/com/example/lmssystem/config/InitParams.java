package com.example.lmssystem.config;

import com.example.lmssystem.entity.Permission;
import com.example.lmssystem.entity.Role;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.enums.Gender;
import com.example.lmssystem.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class InitParams implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final BranchRepository branchRepository;



    @Override
    public void run(String... args) throws Exception {

        Permission permission = permissionRepository.save(
                Permission.builder()
                        .name("ALL PERMISSIONS")
                      .build()
      );
        List<Permission> permissions = new ArrayList<>();
        permissions.add(permission);
        System.out.println(roleRepository.save(
                Role.builder()
                        .name("USER")
                        .permissions(new ArrayList<>())
                        .build()
        ));
        Role role = roleRepository.save(
                Role.builder()
                        .name("SUPER ADMIN")
                        .permissions(permissions)
                        .build()
        );
        System.out.println(role);

        User authUser = User.builder()
                .phoneNumber("+998900771820")
                .passwordSize(8)
                .canLogin(true)
                .birthDate(new Timestamp(System.currentTimeMillis()))
                .deleted(false)
                .firstName("Abbos")
              .lastName("Rejabov")
                .password(passwordEncoder.encode("admin123"))
                .username("admin")
                .gender(Gender.MALE)
                .role(List.of(role))
                .locale("en")
                .branches(null)
                .build();
      userRepository.save(authUser);

  }
}