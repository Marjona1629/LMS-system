package com.example.lmssystem.service;

import com.example.lmssystem.entity.*;
import com.example.lmssystem.enums.Gender;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private RoleServise roleServise;
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
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        user.getGender() != null ? user.getGender().name() : null,
                        user.getBirthDate() != null ? user.getBirthDate() : null,
                        user.getBranches() != null ? user.getBranches().stream().map(Branch::getName).toList() : null,
                        user.getRole() != null ? user.getRole().stream().map(Role::getName).toList() : null
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
            throw new IllegalArgumentException(Utils.getMessage("user.missingBranchAssignment"));
        }

        return userRepository.save(user);
    }

    public List<CreateUserDTO> searchEmployees(Long id, String firstName, String lastName, String phoneNumber) {
        List<User> users = userRepository.searchEmployees(id, firstName, lastName, phoneNumber);
        return users.stream()
                .map(this::convertToCreateUserDTO)
                .toList();
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

    public Optional<User> updateEmployee(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(Utils.getMessage("user.notFound", id)));

        updateFieldIfNotNull(updatedUser.getFirstName(), existingUser::setFirstName, "user.firstName.required");
        updateFieldIfNotNull(updatedUser.getLastName(), existingUser::setLastName, "user.lastName.required");
        updateFieldIfNotNull(updatedUser.getPhoneNumber(), existingUser::setPhoneNumber, "user.phoneNumber.required");

        if (updatedUser.getBranches() != null) {
            List<Branch> validBranches = validateBranches(updatedUser.getBranches());
            existingUser.setBranches(validBranches);
        }

        if (updatedUser.getPassword() != null) {
            if (updatedUser.getPassword().length() < 6) {
                throw new IllegalArgumentException(Utils.getMessage("user.password.required"));
            }
            existingUser.setPassword(updatedUser.getPassword());
        }

        updateFieldIfNotNull(updatedUser.getRole(), existingUser::setRole);
        updateFieldIfNotNull(updatedUser.getGender(), existingUser::setGender);
        updateFieldIfNotNull(updatedUser.getBirthDate(), existingUser::setBirthDate);

        return Optional.of(userRepository.save(existingUser));
    }

    public List<User> getArchivedEmployees() {
        return userRepository.findByDeletedTrue();
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
        if (branches == null || branches.isEmpty()) {
            throw new IllegalArgumentException("User must be assigned to at least one valid branch.");
        }

        List<Branch> validBranches = branches.stream()
                .filter(branch -> branch != null && branch.isValid())
                .collect(Collectors.toList());

        if (validBranches.isEmpty()) {
            throw new IllegalArgumentException("None of the provided branches are valid.");
        }
        return validBranches;
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

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getActiveTeachers() {
        return userRepository.findByRole_Name("TEACHER");
    }

    public CreateUserDTO mapToCreateUserDTO(User user) {
        List<String> branchNames = (user.getBranches() != null)
                ? user.getBranches().stream()
                .map(Branch::getName)
                .collect(Collectors.toList())
                : null;

        List<String> roleNames = (user.getRole() != null)
                ? user.getRole().stream()
                .map(Role::getName)
                .collect(Collectors.toList())
                : null;

        LocalDate birthDate = convertToLocalDate(user.getBirthDate());
        return new CreateUserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                birthDate != null ? birthDate.toString() : null,
                user.getBirthDate() != null ? user.getBirthDate() : null,
                branchNames,
                roleNames
        );
    }

    public LocalDate convertToLocalDate(Date timestamp) {
        return timestamp != null ? timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    private CreateUserDTO convertToCreateUserDTO(User user) {
        List<String> branchNames = (user.getBranches() != null)
                ? user.getBranches().stream()
                .map(Branch::getName)
                .collect(Collectors.toList())
                : null;

        List<String> roleNames = (user.getRole() != null)
                ? user.getRole().stream()
                .map(Role::getName)
                .collect(Collectors.toList())
                : null;

        return new CreateUserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getGender() != null ? user.getGender().name() : null,
                user.getBirthDate() != null ? user.getBirthDate() : null,
                branchNames,
                roleNames
        );
    }

    public List<User> getOtherActiveEmployees() {
        return userRepository.findByRole_NameNotIn(List.of("TEACHER", "ADMIN", "STUDENT"));
    }

    public User convertToUser(CreateUserDTO createUserDTO) {
        List<Branch> allBranches = branchService.findAll();
        List<Role> allRoles = roleServise.findAll();

        Gender gender = Gender.valueOf(createUserDTO.gender().toUpperCase());

        List<Branch> branches = allBranches.stream()
                .filter(branch -> createUserDTO.branchNames().contains(branch.getName()))
                .collect(Collectors.toList());
        List<Role> roles = allRoles.stream()
                .filter(role -> createUserDTO.roles().contains(role.getName()))
                .collect(Collectors.toList());

        return User.builder()
                .firstName(createUserDTO.firstName())
                .lastName(createUserDTO.lastName())
                .phoneNumber(createUserDTO.phoneNumber())
                .gender(gender)
                .birthDate(createUserDTO.birthDate())
                .branches(branches)
                .role(roles)
                .canLogin(true)
                .deleted(false)
                .locale("en")
                .build();
    }
}