package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Modifying
    @Query("update Invoice set status='DELETED' where id=:id")
    void delete(Long id);
}