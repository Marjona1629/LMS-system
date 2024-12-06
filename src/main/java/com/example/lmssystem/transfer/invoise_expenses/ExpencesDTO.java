package com.example.lmssystem.transfer.invoise_expenses;

import java.time.LocalDateTime;

public record ExpencesDTO(String Name, LocalDateTime date, Long categoryId,Long userId,Double amount) {
}
