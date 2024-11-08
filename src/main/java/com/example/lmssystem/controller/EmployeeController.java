package com.example.lmssystem.controller;

import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.service.BranchService;
import com.example.lmssystem.service.UserService;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import com.example.lmssystem.utils.Utils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private UserService userService;
    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseData showEmployees() {
        List<CreateUserDTO> employees = userService.getAllEmployees().stream()
                .map(dto -> new CreateUserDTO(
                        dto.id(),
                        dto.firstName(),
                        dto.lastName(),
                        dto.phoneNumber(),
                        dto.gender(),
                        dto.birthDate(),
                        dto.branchId(),
                        null,
                        dto.role()
                ))
                .collect(Collectors.toList());

        if (employees.isEmpty()) {
            return ResponseData.builder()
                    .success(false)
                    .message(Utils.getMessage("user.noEmployeesFound"))
                    .build();
        }
        return ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("employees_retrieved_success"))
                .data(employees)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData showEmployee(@PathVariable Long id) {
        Optional<User> employee = userService.getEmployeeById(id);
        return employee.map(user -> ResponseData.builder()
                        .success(true)
                        .message(Utils.getMessage("employee_retrieved_success"))
                        .data(user)
                        .build())
                .orElseGet(() -> ResponseData.builder()
                        .success(false)
                        .message(Utils.getMessage("employee_not_found"))
                        .data(null)
                        .build());
    }

    @PostMapping
    @SneakyThrows
    public ResponseData addEmployee(@RequestBody CreateUserDTO createUserDTO) {
        User user = convertToUser(createUserDTO);
        User addedEmployee = userService.addEmployee(user);

        return ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("employee_added_success"))
                .data(addedEmployee)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteEmployee(@PathVariable Long id) {
        boolean isDeleted = userService.softDeleteEmployee(id);
        return isDeleted ? ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("employee_deleted_success"))
                .data(null)
                .build() : ResponseData.builder()
                .success(false)
                .message(Utils.getMessage("employee_not_found"))
                .data(null)
                .build();
    }

    @PutMapping("/{id}")
    @SneakyThrows
    public ResponseEntity<ResponseData> changeEmployee(@PathVariable Long id, @RequestBody CreateUserDTO updateUserDTO) {
        User updatedUser = convertToUser(updateUserDTO);
        Optional<User> updatedEmployee = userService.updateEmployee(id, updatedUser);

        if (updatedEmployee.isPresent()) {
            return ResponseEntity.ok(ResponseData.builder()
                    .success(true)
                    .message(Utils.getMessage("employee_updated_success"))
                    .data(updatedEmployee.get())
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseData.builder()
                    .success(false)
                    .message(Utils.getMessage("employee_not_found"))
                    .data(null)
                    .build());
        }
    }

    private List<Branch> fetchBranchesById(Long branchId) {
        Branch branch = branchService.getBranchById(branchId).orElse(null);
        return branch != null ? List.of(branch) : List.of();
    }
    private User convertToUser(CreateUserDTO createUserDTO) {
        return User.builder()
                .firstName(createUserDTO.firstName())
                .lastName(createUserDTO.lastName())
                .phoneNumber(createUserDTO.phoneNumber())
                .gender(createUserDTO.gender())
                .birthDate(createUserDTO.birthDate() != null ? java.sql.Date.valueOf(createUserDTO.birthDate()) : null)
                .branches(fetchBranchesById(createUserDTO.branchId()))
                .build();
    }
}