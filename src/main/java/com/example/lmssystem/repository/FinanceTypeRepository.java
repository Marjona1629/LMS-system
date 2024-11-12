package com.example.lmssystem.repository;

import com.example.lmssystem.entity.FinanceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinanceTypeRepository extends JpaRepository<FinanceType, Long> {
    Optional<FinanceType> findByName(String typeName);
}
