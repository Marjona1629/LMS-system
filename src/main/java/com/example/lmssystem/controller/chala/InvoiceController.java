package com.example.lmssystem.controller.chala;

import com.example.lmssystem.entity.Invoice;
import com.example.lmssystem.service.InvoiceService;
import com.example.lmssystem.transfer.invoise_expenses.InvoiceUpdateDTO;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.invoise_expenses.InvoiceCreateDTO;

import com.example.lmssystem.transfer.invoise_expenses.InvoiceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {
  private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceCreateDTO invoiceCreateDTO) {
        Invoice newInvoice = invoiceService.saveInvoice(invoiceCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.builder()
                        .message("Invoice created successfully")
                        .data(newInvoice)
                        .success(true)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(ResponseData.builder()
                .message("Invoices retrieved successfully")
                .data(invoices)
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        InvoiceDTO invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message("Invoice found with id: " + id)
                .data(invoice)
                .success(true)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Long id, @RequestBody InvoiceUpdateDTO invoiceCreateDTO) {
        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceCreateDTO.amount(),invoiceCreateDTO.status());
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