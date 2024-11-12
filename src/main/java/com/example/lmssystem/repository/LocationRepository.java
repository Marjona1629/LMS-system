package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Long> {
}
