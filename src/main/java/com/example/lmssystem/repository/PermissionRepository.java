package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
    boolean existsByName(String name);
}