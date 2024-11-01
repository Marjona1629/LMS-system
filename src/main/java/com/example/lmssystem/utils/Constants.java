package com.example.lmssystem.utils;

public class Constants {
    public static String[] whiteList = {
            "/v1/auth/login",
            "/v1/auth/image/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/webjars/**"
    };
}
