package com.example.lmssystem.service;

import com.example.lmssystem.entity.Invoice;
import com.example.lmssystem.repository.InvoiceRepository;
import com.example.lmssystem.transfer.InvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice saveInvoice(InvoiceDTO invoiceDTO) {
        return null;
    }

    public List<Invoice> getAllInvoices() {
        return null;
    }

    public Invoice getInvoiceById(Long id) {
        return null;
    }

    public Invoice updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        return null;
    }

    public void deleteInvoice(Long id) {

    }
}