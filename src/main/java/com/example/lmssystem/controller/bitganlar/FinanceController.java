package com.example.lmssystem.controller.bitganlar;

import com.example.lmssystem.entity.Finance;
import com.example.lmssystem.service.FinanceService;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.finance.FinanceCreateDTO;
import com.example.lmssystem.transfer.finance.FinanceUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/finance")
@RequiredArgsConstructor
public class FinanceController {
    private final FinanceService financeService;

    @PostMapping
    public ResponseEntity<?> createFinance(@RequestBody FinanceCreateDTO finance) {
        Finance createdFinance = financeService.createFinance(finance);
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(createdFinance)
                .success(true)
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllFinances() {
        List<Finance> finances = financeService.getAllFinances();
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(finances)
                .success(true)
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFinanceById(@PathVariable Long id) {
        Finance finance = financeService.getFinanceById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(finance)
                .success(true)
                .build()
        );    }

    @PutMapping("/{id}/minus")
    public ResponseEntity<?> minusFinance(@PathVariable Long id, @RequestBody FinanceUpdateDTO financeUpdateDTO) {
        Finance finance = financeService.minusUpdateFinance(id, financeUpdateDTO);
        return ResponseEntity.status(200).body(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(finance)
                .success(true)
                .build()
        );
    }

    @PutMapping("/{id}/plus")
    public ResponseEntity<?> plusFinance(@PathVariable Long id, @RequestBody FinanceUpdateDTO financeUpdateDTO) {
        Finance finance = financeService.plusUpdateFinance(id, financeUpdateDTO);
        return ResponseEntity.status(200).body(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .data(finance)
                .success(true)
                .build()
        );
    }

}
