package com.example.lmssystem.service;

import com.example.lmssystem.entity.User;
import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BranchService branchService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<CreateUserDTO> getAllEmployees() {
        List<User> employees = userRepository.findAll();
        if (employees.isEmpty()) {
            throw new IllegalStateException(Utils.getMessage("user.noEmployeesFound"));
        }
        return employees.stream()
                .map(user -> new CreateUserDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        null,
                        null,
                        null,
                        null,
                        user.getRole()
                ))
                .toList();
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
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.firstName.required"));
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.lastName.required"));
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.phone.required"));
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.password.required"));
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new IllegalArgumentException(Utils.getMessage("user.role.required"));
        }
        if (user.getBirthDate() == null) {
            throw new IllegalArgumentException(Utils.getMessage("user.birthDate.required"));
        }
        if (user.getGender() == null) {
            throw new IllegalArgumentException(Utils.getMessage("user.gender.required"));
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            throw new IllegalArgumentException(Utils.getMessage("user.phone.invalid"));
        }

        if (user.getBirthDate().after(new Date())) {
            throw new IllegalArgumentException(Utils.getMessage("user.birthDate.future"));
        }
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?[1-9]\\d{1,14}$";
        return phoneNumber != null && phoneNumber.matches(regex);
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

            updateFieldIfNotNull(updatedUser.getFirstName(), existingUser::setFirstName, "user.firstName.required");
            updateFieldIfNotNull(updatedUser.getLastName(), existingUser::setLastName, "user.lastName.required");
            updateFieldIfNotNull(updatedUser.getPhoneNumber(), existingUser::setPhoneNumber, "user.phoneNumber.required");

            if (updatedUser.getBranches() != null && !updatedUser.getBranches().isEmpty()) {
                List<Branch> validBranches = validateBranches(updatedUser.getBranches());
                existingUser.setBranches(validBranches);
            }

            if (updatedUser.getPassword() != null && updatedUser.getPassword().length() >= 6) {
                existingUser.setPassword(updatedUser.getPassword());
            } else {
                throw new IllegalArgumentException(Utils.getMessage("user.password.required"));
            }

            updateFieldIfNotNull(updatedUser.getRole(), existingUser::setRole);
            updateFieldIfNotNull(updatedUser.getGender(), existingUser::setGender);
            updateFieldIfNotNull(updatedUser.getBirthDate(), existingUser::setBirthDate);

            return Optional.of(userRepository.save(existingUser));
        }
        return Optional.empty();
    }
    private <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
    private void updateFieldIfNotNull(String value, Consumer<String> setter, String errorMessage) {
        if (value != null && !value.trim().isEmpty()) {
            setter.accept(value);
        } else if (errorMessage != null) {
            throw new IllegalArgumentException(Utils.getMessage(errorMessage));
        }
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

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}