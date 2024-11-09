package com.example.lmssystem.controller;

import com.example.lmssystem.service.AuthServise;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthServise authServise;

    @PostMapping("/create")
    @SneakyThrows
    @PreAuthorize(value = "hasAnyAuthority('CREATOR')")
    public ResponseEntity<?> create(@RequestBody CreateUserDTO createUserDTO) {
        return authServise.createUser(createUserDTO);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn( String username, String password) {

        return authServise.signIn(username, password);
    }
}