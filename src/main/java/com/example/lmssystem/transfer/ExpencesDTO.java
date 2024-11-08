package com.example.lmssystem.trnasfer;

import com.example.lmssystem.entity.Category;

import java.time.LocalDateTime;

public record ExpencesDTO(String Name, LocalDateTime date, Long categoryId,Long userId,Double amount) {
}
