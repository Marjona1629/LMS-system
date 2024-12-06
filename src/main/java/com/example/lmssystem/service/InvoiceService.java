package com.example.lmssystem.service;

import com.example.lmssystem.entity.Invoice;
import com.example.lmssystem.enums.InvoiceStatus;
import com.example.lmssystem.repository.GroupRepository;
import com.example.lmssystem.repository.InvoiceRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.invoise_expenses.InvoiceDTO;
import com.example.lmssystem.transfer.invoise_expenses.InvoiceCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public Invoice saveInvoice(InvoiceCreateDTO invoiceCreateDTO) {
        Invoice build = Invoice.builder()
                .user(userRepository.findById(invoiceCreateDTO.userId()).orElseThrow())
                .amount(invoiceCreateDTO.amount())
                .group(groupRepository.findById(invoiceCreateDTO.groupId()).orElseThrow())
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .status(InvoiceStatus.WAITING)
                .build();
        return invoiceRepository.save(build);
    }

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(invoice -> {
            return InvoiceDTO.builder()
                    .createTime(invoice.getDate().toLocalDateTime())
                    .amount(invoice.getAmount())
                    .invoiceStatus(invoice.getStatus().toString())
                    .groupId(invoice.getGroup().getId())
                    .id(invoice.getId())
                    .userId(invoice.getUser().getId())
                    .build();
        }).toList();
    }

    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();

        return InvoiceDTO.builder()
                .createTime(invoice.getDate().toLocalDateTime())
                .amount(invoice.getAmount())
                .invoiceStatus(invoice.getStatus().toString())
                .groupId(invoice.getGroup().getId())
                .id(invoice.getId())
                .userId(invoice.getUser().getId())
                .build();
    }

    public Invoice updateInvoice(Long id, Double amount,String status) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setAmount(amount>=0?amount:invoice.getAmount());
        invoice.setStatus(Arrays.stream(InvoiceStatus.values()).toList().contains(status.toLowerCase())?InvoiceStatus.valueOf(status.toUpperCase()):invoice.getStatus());
        return invoiceRepository.save(invoice);
    }

    @Transient
    public void deleteInvoice(Long id) {
        invoiceRepository.delete(id);
    }
}