package com.example.lmssystem.trnasfer.auth;

import com.example.lmssystem.enums.Gender;

import java.util.List;

public record CreateUserDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        Gender gender,
        String birthDate,
        Long branchId,
        String password,
        List<String> role
) {
}