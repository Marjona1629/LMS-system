package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {
    List<Finance> findAllByUser_IdAndType_Id(Long userId, Long typeId);
    Optional<Finance> findByUser_IdAndType_Id(Long userId, Long userId1);
}

