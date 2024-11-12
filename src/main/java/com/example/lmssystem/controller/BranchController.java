package com.example.lmssystem.controller;

import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.repository.BranchRepository;
import com.example.lmssystem.servise.BranchService;
import com.example.lmssystem.trnasfer.BranchDTO;
import com.example.lmssystem.trnasfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/branch")
public class BranchController {
    private final BranchService branchService;
    private final BranchRepository branchRepository;

    @GetMapping
    public ResponseEntity<?> getBranchs() {
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(branchService.getAllBranches())
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBranch(@PathVariable Long id) {
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(branchService.getBranchById(id))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }
    @PostMapping
    public ResponseEntity<?> createBranch(@RequestBody String branch) {
        Branch branch1=new Branch();
        branch1.setName(branch);
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(branchRepository.save(branch1))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable Long id,@RequestBody String branch) {
        Branch branch1 = branchRepository.findById(id).orElseThrow();
        branch1.setName(branch);
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(branchRepository.save(branch1))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }
}
