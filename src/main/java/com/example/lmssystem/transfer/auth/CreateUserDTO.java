package com.example.lmssystem.transfer.auth;

import java.sql.Timestamp;
import java.util.List;

public record CreateUserDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        String gender,
        Timestamp birthDate,
        List<String> branchNames,
        List<String> roles
) {
}