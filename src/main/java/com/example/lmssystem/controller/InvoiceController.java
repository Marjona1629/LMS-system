package com.example.lmssystem.controller;

import com.example.lmssystem.entity.Invoice;
import com.example.lmssystem.servise.InvoiceService;
import com.example.lmssystem.trnasfer.InvoiceDTO;
import com.example.lmssystem.trnasfer.ResponseData;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {
@Autowired
  private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        Invoice newInvoice = invoiceService.saveInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.builder()
                        .message("Invoice created successfully")
                        .data(newInvoice)
                        .success(true)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(ResponseData.builder()
                .message("Invoices retrieved successfully")
                .data(invoices)
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message("Invoice found with id: " + id)
                .data(invoice)
                .success(true)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDTO) {
        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
        return ResponseEntity.ok(ResponseData.builder()
                .message("Invoice updated successfully")
                .data(updatedInvoice)
                .success(true)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message("Invoice deleted successfully")
                .success(true)
                .build());
    }
}
