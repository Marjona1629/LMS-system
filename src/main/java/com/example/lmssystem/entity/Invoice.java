package com.example.lmssystem.entity;

import com.example.lmssystem.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;
    private Double amount;
    @ManyToOne
    private User user;
    @ManyToOne
    private Group group;
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

}
