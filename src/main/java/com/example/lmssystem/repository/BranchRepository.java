package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}