package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaysRepository extends JpaRepository<Day,Long> {
}