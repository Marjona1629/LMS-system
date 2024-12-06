package com.example.lmssystem.service;

import com.example.lmssystem.entity.Expences;
import com.example.lmssystem.repository.ExpencesRepository;
import com.example.lmssystem.transfer.invoise_expenses.ExpencesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpencesService {

    @Autowired
    private ExpencesRepository expencesRepository;

    public Expences saveExpences(ExpencesDTO expences) {
        return null;
    }

    public List<Expences> getAllExpences() {
        return null;
    }

    public Expences getExpencesById(Long id) {
        return null;
    }

    public Expences updateExpences(Long id, ExpencesDTO expencesDTO) {
       return null;
    }

    public void deleteExpences(Long id) {

    }
}