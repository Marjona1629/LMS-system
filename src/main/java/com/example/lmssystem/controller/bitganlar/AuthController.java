package com.example.lmssystem.controller.bitganlar;

import com.example.lmssystem.service.AuthServise;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthServise authServise;

    @PostMapping("/create")
    @SneakyThrows
    public ResponseEntity<?> create(@RequestBody CreateUserDTO createUserDTO) {
        return authServise.createUser(createUserDTO);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn( String username, String password) {
        return authServise.signIn(username, password);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return authServise.getUser(id);
    }
    @GetMapping
    public ResponseEntity<?> getUsers() {
        return authServise.getUsers();
    }
}