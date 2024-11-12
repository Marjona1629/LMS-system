package com.example.lmssystem.controller;


import com.example.lmssystem.servise.FinanceService;
import com.example.lmssystem.trnasfer.ResponseData;
import com.example.lmssystem.trnasfer.auth.FinanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance")
public class FinanceController {
    private final FinanceService financeService;

    @Autowired
    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @PostMapping
    public ResponseEntity<?> createFinance(@RequestBody FinanceDTO financeDTO) {
        FinanceDTO createdFinance = financeService.createFinance(financeDTO);
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(createdFinance)
                .success(true)
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllFinances() {
        List<FinanceDTO> finances = financeService.getAllFinances();
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(finances)
                .success(true)
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFinanceById(@PathVariable Long id) {
        FinanceDTO finance = financeService.getFinanceById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(finance)
                .success(true)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFinance(@PathVariable Long id, @RequestBody FinanceDTO financeDTO) {
        FinanceDTO updatedFinance = financeService.updateFinance(id, financeDTO);
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(updatedFinance)
                .success(true)
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFinance(@PathVariable Long id) {
        financeService.deleteFinance(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .success(true)
                .build()
        );
    }
}
