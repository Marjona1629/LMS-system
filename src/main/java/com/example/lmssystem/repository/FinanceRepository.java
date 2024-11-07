package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Long> {
    @Query("select f from Finance  f where f.user.id=:id and f.type.id=1")
    Optional<Finance> findByUser(Long id);
}

