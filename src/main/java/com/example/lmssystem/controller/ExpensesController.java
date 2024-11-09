package com.example.lmssystem.controller;

import com.example.lmssystem.entity.Expences;
import com.example.lmssystem.service.ExpencesService;
import com.example.lmssystem.transfer.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpensesController {

    @Autowired
    private ExpencesService expencesService;

    @PostMapping
    public ResponseEntity<?> createExpenses(@RequestBody com.example.lmssystem.trnasfer.ExpencesDTO expencesDTO) {
        Expences newExpences = expencesService.saveExpences(expencesDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.builder()
                        .message("Expense created successfully")
                        .data(newExpences)
                        .success(true)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllExpences() {
        List<Expences> expencesList = expencesService.getAllExpences();
        return ResponseEntity.ok(ResponseData.builder()
                .message("Expenses retrieved successfully")
                .data(expencesList)
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpensesById(@PathVariable Long id) {
        Expences expences = expencesService.getExpencesById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message("Expense found with id: " + id)
                .data(expences)
                .success(true)
                .build());
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateExpenses(@PathVariable Long id, @RequestBody  ExpensesDTO expensesDTO) {
//        Expenses updatedExpenses = expensesService.updateExpenses(id, expensesDTO);
//        return ResponseEntity.ok(ResponseData.builder()
//                .message("Expense updated successfully")
//                .data(updatedExpenses)
//                .success(true)
//                .build());
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteExpenses(@PathVariable Long id) {
//        expensesService.deleteExpenses(id);
//        return ResponseEntity.ok(ResponseData.builder()
//                .message("Expense deleted successfully")
//                .success(true)
//                .build());
//    }
}
