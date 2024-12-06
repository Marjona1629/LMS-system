package com.example.lmssystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.lmssystem.entity")
@EnableJpaRepositories("com.example.lmssystem.repository")
public class LmsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsSystemApplication.class, args);
    }

}