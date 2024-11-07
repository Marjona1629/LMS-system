package com.example.lmssystem.servise;

import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }
}