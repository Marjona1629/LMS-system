package com.example.lmssystem.controller;

import com.example.lmssystem.entity.User;
import com.example.lmssystem.service.UserService;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseData> getUserProfile(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        return user.map(u -> ResponseEntity.ok(ResponseData.builder()
                        .success(true)
                        .message(Utils.getMessage("user.profile.retrieved_success"))
                        .data(u)
                        .build()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseData.builder()
                                .success(false)
                                .message(Utils.getMessage("user.profile.not_found", id))
                                .data(null)
                                .build()));
    }
}