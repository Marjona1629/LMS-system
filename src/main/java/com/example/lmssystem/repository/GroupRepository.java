package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}