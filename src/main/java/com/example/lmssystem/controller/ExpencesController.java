package com.example.lmssystem.controller;

import com.example.lmssystem.entity.Expences;
import com.example.lmssystem.servise.ExpencesService;
import com.example.lmssystem.trnasfer.ExpencesDTO;
import com.example.lmssystem.trnasfer.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpencesController {

    @Autowired
    private ExpencesService expencesService;

    @PostMapping
    public ResponseEntity<?> createExpences(@RequestBody ExpencesDTO expencesDTO) {
        Expences newExpences = expencesService.saveExpences(expencesDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.builder()
                        .message("Expence created successfully")
                        .data(newExpences)
                        .success(true)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllExpences() {
        List<Expences> expencesList = expencesService.getAllExpences();
        return ResponseEntity.ok(ResponseData.builder()
                .message("Expences retrieved successfully")
                .data(expencesList)
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpencesById(@PathVariable Long id) {
        Expences expences = expencesService.getExpencesById(id);
        return ResponseEntity.ok(ResponseData.builder()
                .message("Expence found with id: " + id)
                .data(expences)
                .success(true)
                .build());
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateExpences(@PathVariable Long id, @RequestBody  ExpencesDTO expencesDTO) {
//        Expences updatedExpences = expencesService.updateExpences(id, expencesDTO);
//        return ResponseEntity.ok(ResponseData.builder()
//                .message("Expence updated successfully")
//                .data(updatedExpences)
//                .success(true)
//                .build());
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteExpences(@PathVariable Long id) {
//        expencesService.deleteExpences(id);
//        return ResponseEntity.ok(ResponseData.builder()
//                .message("Expence deleted successfully")
//                .success(true)
//                .build());
//    }
}
