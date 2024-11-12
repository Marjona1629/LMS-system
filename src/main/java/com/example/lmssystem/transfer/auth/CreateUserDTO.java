package com.example.lmssystem.transfer.auth;

import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.entity.Role;
import com.example.lmssystem.enums.Gender;

import java.util.List;

public record CreateUserDTO(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        Gender gender,
        String birthDate,
        Branch branch,
        String password,
        List<Role> role
) {
}