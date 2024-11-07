package com.example.lmssystem.servise;

import com.example.lmssystem.entity.User;
import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BranchService branchService;

    public List<User> getAllEmployees() {
        List<User> employees = userRepository.findAll();
        if (employees.isEmpty()) {
            throw new IllegalStateException(Utils.getMessage("user.noEmployeesFound"));
        }
        return employees;
    }

    public Optional<User> getEmployeeById(Long id) {
        Optional<User> employee = userRepository.findById(id);
        if (employee.isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.employee.notFound", id));
        }
        return employee;
    }

    public User addEmployee(User user) {
        validateEmployeeData(user);

        if (user.getBranches() != null && !user.getBranches().isEmpty()) {
            List<Branch> validBranches = validateBranches(user.getBranches());
            user.setBranches(validBranches);
        } else {
            throw new IllegalArgumentException(Utils.getMessage("user.branches.empty"));
        }

        return userRepository.save(user);
    }

    private void validateEmployeeData(User user) {
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.firstName.required"));
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.lastName.required"));
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.phoneNumber.required"));
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException(Utils.getMessage("user.password.required"));
        }
    }

    private List<Branch> validateBranches(List<Branch> branches) {
        List<Branch> validBranches = new ArrayList<>();
        for (Branch branch : branches) {
            Branch foundBranch = branchService.getBranchById(branch.getId()).orElse(null);
            if (foundBranch != null) {
                validBranches.add(foundBranch);
            } else {
                throw new IllegalArgumentException(Utils.getMessage("user.branches.invalid", branch.getId()));
            }
        }
        return validBranches;
    }

    public Optional<User> updateEmployee(Long id, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().trim().isEmpty()) {
                existingUser.setFirstName(updatedUser.getFirstName());
            } else {
                throw new IllegalArgumentException(Utils.getMessage("user.firstName.required"));
            }
            if (updatedUser.getLastName() != null && !updatedUser.getLastName().trim().isEmpty()) {
                existingUser.setLastName(updatedUser.getLastName());
            } else {
                throw new IllegalArgumentException(Utils.getMessage("user.lastName.required"));
            }

            if (updatedUser.getPhoneNumber() != null && !updatedUser.getPhoneNumber().trim().isEmpty()) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            } else {
                throw new IllegalArgumentException(Utils.getMessage("user.phoneNumber.required"));
            }

            if (updatedUser.getBranches() != null && !updatedUser.getBranches().isEmpty()) {
                List<Branch> validBranches = validateBranches(updatedUser.getBranches());
                existingUser.setBranches(validBranches);
            }

            if (updatedUser.getPassword() != null && updatedUser.getPassword().length() >= 6) {
                existingUser.setPassword(updatedUser.getPassword());
            } else {
                throw new IllegalArgumentException(Utils.getMessage("user.password.required"));
            }

            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }
            if (updatedUser.getGender() != null) {
                existingUser.setGender(updatedUser.getGender());
            }
            if (updatedUser.getBirthDate() != null) {
                existingUser.setBirthDate(updatedUser.getBirthDate());
            }

            return Optional.of(userRepository.save(existingUser));
        }
        return Optional.empty();
    }

    public boolean softDeleteEmployee(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.softDeleteById(id);
            return true;
        } else {
            throw new IllegalArgumentException(Utils.getMessage("user.employee.notFound", id));
        }
    }
}