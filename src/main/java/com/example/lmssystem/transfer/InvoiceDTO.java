package com.example.lmssystem.trnasfer;

import java.time.LocalDateTime;

public record InvoiceDTO(LocalDateTime date,Double amount,Long userId,Long groupId,String status) {
}
