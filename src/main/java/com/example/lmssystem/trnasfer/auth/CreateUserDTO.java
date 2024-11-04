package com.example.lmssystem.trnasfer.auth;

public record CreateUserDTO (String firstName, String lastName, String phoneNumber, String gender,String birthDate,Long branchId) {
}
