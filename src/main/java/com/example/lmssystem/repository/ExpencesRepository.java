package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Expences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpencesRepository extends JpaRepository<Expences, Long> {

}