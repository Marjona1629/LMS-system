package com.example.lmssystem.transfer.auth;

import java.sql.Timestamp;
import java.util.List;

public record CreateEmployeeDTO(
        String username,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        String gender,
        Timestamp birthDate,
        List<String> branchNames,
        List<String> roles
) {
}