package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Object> findByName(String branchName);
}