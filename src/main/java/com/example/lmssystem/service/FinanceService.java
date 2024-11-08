package com.example.lmssystem.service;

import com.example.lmssystem.entity.Finance;
import com.example.lmssystem.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FinanceService {

    private final FinanceRepository financeRepository;

    @Autowired
    public FinanceService(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    public Finance createFinance(Finance finance) {
        return financeRepository.save(finance);
    }

    public List<Finance> getAllFinances() {
        return financeRepository.findAll();
    }

    public Finance getFinanceById(Long id) {
        return financeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance not found with id " + id));
    }

    public Finance updateFinance(Long id, Finance updatedFinance) {
        return financeRepository.findById(id).map(finance -> {
            finance.setUser(updatedFinance.getUser());
            finance.setType(updatedFinance.getType());
            finance.setAmount(updatedFinance.getAmount());
            return financeRepository.save(finance);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance not found with id " + id));
    }

    public void deleteFinance(Long id) {
        if (!financeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance not found with id " + id);
        }
        financeRepository.deleteById(id);
    }
}