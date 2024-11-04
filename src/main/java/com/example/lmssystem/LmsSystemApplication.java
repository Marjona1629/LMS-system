package com.example.lmssystem;

import com.example.lmssystem.entity.Permission;
import com.example.lmssystem.entity.Role;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.enums.Gender;
import com.example.lmssystem.repository.BranchRepository;
import com.example.lmssystem.repository.PermittionRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication

public class LmsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsSystemApplication.class, args);

    }



}
