package com.example.lmssystem.transfer;

import com.example.lmssystem.entity.Category;

import java.time.LocalDateTime;

public record ExpencesDTO(String Name, LocalDateTime date, Long categoryId,Long userId,Double amount) {
}
