package com.example.lmssystem.transfer.invoise_expenses;

import com.example.lmssystem.enums.InvoiceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceDTO {
    private Long id;
    private Long userId;
    private Double amount;
    private Long groupId;
    private String invoiceStatus;
    private LocalDateTime createTime;
}
