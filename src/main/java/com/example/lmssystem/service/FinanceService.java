package com.example.lmssystem.service;

import com.example.lmssystem.entity.Finance;
import com.example.lmssystem.entity.FinanceType;
import com.example.lmssystem.repository.FinaceTypeRepository;
import com.example.lmssystem.repository.FinanceRepository;
import com.example.lmssystem.repository.UserRepository;

import com.example.lmssystem.transfer.finance.FinanceCreateDTO;
import com.example.lmssystem.transfer.finance.FinanceUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FinanceRepository financeRepository;
    private final FinaceTypeRepository finaceTypeRepository;
    private final UserRepository userRepository;


    public Finance createFinance(FinanceCreateDTO finance) {
        Finance finance1 = new Finance();
        finance1.setAmount(finance.amount() > 0 ? finance.amount() : 0);
        finance1.setType(finaceTypeRepository.findById(finance.financeType()).orElseThrow());
        finance1.setUser(userRepository.findById(finance.owner()).orElseThrow());
        if (financeRepository.findAllByUser_IdAndType_Id(finance.owner(),finance.financeType()).isEmpty()) {
            return financeRepository.save(finance1);
        }
        return null;
    }

    public List<Finance> getAllFinances() {
        return financeRepository.findAll();
    }

    public Finance getFinanceById(Long id) {
        return financeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance not found with id " + id));
    }

    public Finance plusUpdateFinance(Long id, FinanceUpdateDTO updatedFinance) {
        Finance byUserId = financeRepository.findByUser_IdAndType_Id(id,updatedFinance.type()).orElseThrow();
        byUserId.setAmount(byUserId.getAmount() + updatedFinance.amount());
        return financeRepository.save(byUserId);
    }

    public Finance minusUpdateFinance(Long id, FinanceUpdateDTO updatedFinance) {
        Finance byUserId = financeRepository.findByUser_IdAndType_Id(id, updatedFinance.type()).orElseThrow();
        byUserId.setAmount(byUserId.getAmount() - updatedFinance.amount());
        return financeRepository.save(byUserId);
    }
}