package com.example.lmssystem.transfer.auth;

public record ProfileDTO(Long id,String firstName,String lastName,String phoneNumber,String role,String branch,Double balance,String password) {
}
