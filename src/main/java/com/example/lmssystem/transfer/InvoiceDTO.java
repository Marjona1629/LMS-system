package com.example.lmssystem.transfer;

import java.time.LocalDateTime;

public record InvoiceDTO(LocalDateTime date,Double amount,Long userId,Long groupId,String status) {
}
