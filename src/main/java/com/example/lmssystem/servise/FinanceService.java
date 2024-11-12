package com.example.lmssystem.servise;

import com.example.lmssystem.entity.Finance;
import com.example.lmssystem.repository.FinanceRepository;
import com.example.lmssystem.repository.FinanceTypeRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.trnasfer.auth.FinanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final FinanceRepository financeRepository;
    private final UserRepository userRepository;
    private final FinanceTypeRepository financeTypeRepository;

    @Autowired
    public FinanceService(FinanceRepository financeRepository, UserRepository userRepository, FinanceTypeRepository financeTypeRepository) {
        this.financeRepository = financeRepository;
        this.userRepository = userRepository;
        this.financeTypeRepository = financeTypeRepository;
    }

    public FinanceDTO createFinance(FinanceDTO financeDTO) {
        Finance finance = new Finance();
        return getFinanceDTO(financeDTO, finance);
    }

    private FinanceDTO getFinanceDTO(FinanceDTO financeDTO, Finance finance) {
        finance.setUser(userRepository.findById(financeDTO.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + financeDTO.userId())));
        finance.setType(financeTypeRepository.findByName(financeDTO.type())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance type not found: " + financeDTO.type())));
        finance.setAmount(financeDTO.amount());
        Finance savedFinance = financeRepository.save(finance);
        return mapToDTO(savedFinance);
    }

    public List<FinanceDTO> getAllFinances() {
        return financeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FinanceDTO getFinanceById(Long id) {
        Finance finance = financeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance not found with id " + id));
        return mapToDTO(finance);
    }

    public FinanceDTO updateFinance(Long id, FinanceDTO financeDTO) {
        Finance finance = financeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance not found with id " + id));

        return getFinanceDTO(financeDTO, finance);
    }

    public void deleteFinance(Long id) {
        if (!financeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Finance not found with id " + id);
        }
        financeRepository.deleteById(id);
    }

    private FinanceDTO mapToDTO(Finance finance) {
        return new FinanceDTO(
                finance.getUser().getId(),
                finance.getType().getName(),
                finance.getAmount()
        );
    }
}