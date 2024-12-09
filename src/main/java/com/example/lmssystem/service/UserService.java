package com.example.lmssystem.service;

import com.example.lmssystem.entity.*;
import com.example.lmssystem.enums.Gender;
import com.example.lmssystem.exception.UserServiceException;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.auth.CreateEmployeeDTO;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
    @Autowired
    private RoleServise roleServise;
    @Autowired
    private BranchService branchService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<CreateUserDTO> getAllEmployees() {
        List<User> employees = userRepository.findAllNonDeleted();
        if (employees.isEmpty()) {
            throw new UserServiceException(Utils.getMessage("user.noEmployeesFound"));
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
            throw new UserServiceException(Utils.getMessage("user.employee.notFound", id));
        }
        return employee;
    }

    public User addEmployee(User user) {
        validateEmployeeData(user);

        if (user.getBranches() == null) {
            user.setBranches(new ArrayList<>());
        }
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            List<Role> validRoles = user.getRole().stream()
                    .map(role -> roleServise.findRoleByName(role.getName()))
                    .collect(Collectors.toList());
            user.setRole(validRoles);
        }

        if (user.getBranches() != null && !user.getBranches().isEmpty()) {
            List<Branch> validBranches = validateBranches(user.getBranches());
            user.setBranches(validBranches);
        }
        return userRepository.save(user);
    }



    private Branch findBranchByName(String branchName) {
        return (Branch) branchService.findByName(branchName)
                .orElseThrow(() -> new UserServiceException("Branch not found: " + branchName));
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
            throw new UserServiceException(Utils.getMessage("user.employee.notFound", id));
        }
    }

    public User updateEmployee(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserServiceException(Utils.getMessage("user.notFound", id)));

        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        if (updatedUser.getBranches() != null && !updatedUser.getBranches().isEmpty()) {
            List<Branch> validBranches = validateBranches(updatedUser.getBranches());
            existingUser.setBranches(validBranches);
        }

        if (updatedUser.getPassword() != null && updatedUser.getPassword().length() >= 6) {
            existingUser.setPassword(updatedUser.getPassword());
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

        return userRepository.save(existingUser);
    }

    public List<User> getArchivedEmployees() {
        return userRepository.findByDeletedTrue();
    }

    private void validateEmployeeData(User user) {
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new UserServiceException(Utils.getMessage("user.firstName.required"));
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new UserServiceException(Utils.getMessage("user.lastName.required"));
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new UserServiceException(Utils.getMessage("user.phone.required"));
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserServiceException(Utils.getMessage("user.password.required"));
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new UserServiceException(Utils.getMessage("user.role.required"));
        }
        if (user.getBirthDate() == null) {
            throw new UserServiceException(Utils.getMessage("user.birthDate.required"));
        }
        if (user.getGender() == null) {
            throw new UserServiceException(Utils.getMessage("user.gender.required"));
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            throw new UserServiceException(Utils.getMessage("user.phone.invalid"));
        }

        if (user.getBirthDate().after(new Date())) {
            throw new UserServiceException(Utils.getMessage("user.birthDate.future"));
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?[1-9]\\d{1,14}$";
        return phoneNumber != null && phoneNumber.matches(regex);
    }

    private List<Branch> validateBranches(List<Branch> branches) {
        if (branches == null || branches.isEmpty()) {
            throw new UserServiceException("User must be assigned to at least one valid branch.");
        }

        List<Branch> validBranches = branches.stream()
                .filter(branch -> branch != null && branch.isValid())
                .toList();

        if (validBranches.isEmpty()) {
            throw new UserServiceException("None of the provided branches are valid.");
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
            throw new UserServiceException(Utils.getMessage(errorMessage));
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

    public User convertToUser(CreateEmployeeDTO createEmployeeDTO) {
        List<Branch> allBranches = branchService.findAll();
        List<Role> allRoles = roleServise.findAll();

        Gender gender = Gender.valueOf(createEmployeeDTO.gender().toUpperCase());

        List<Branch> branches = allBranches.stream()
                .filter(branch -> createEmployeeDTO.branchNames().contains(branch.getName()))
                .collect(Collectors.toList());
        List<Role> roles = allRoles.stream()
                .filter(role -> createEmployeeDTO.roles().contains(role.getName()))
                .collect(Collectors.toList());
        System.out.println("Filtered Roles: " + roles);

        return User.builder()
                .username(createEmployeeDTO.username())
                .password(createEmployeeDTO.password())
                .firstName(createEmployeeDTO.firstName())
                .lastName(createEmployeeDTO.lastName())
                .phoneNumber(createEmployeeDTO.phoneNumber())
                .gender(gender)
                .birthDate(createEmployeeDTO.birthDate())
                .branches(branches)
                .role(roles)
                .canLogin(true)
                .deleted(false)
                .locale("en")
                .build();
    }
}