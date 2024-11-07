package com.example.lmssystem.controller;

import com.example.lmssystem.entity.Branch;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.servise.BranchService;
import com.example.lmssystem.servise.UserService;
import com.example.lmssystem.trnasfer.ResponseData;
import com.example.lmssystem.trnasfer.auth.CreateUserDTO;
import com.example.lmssystem.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private UserService userService;
    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseData showEmployees() {
        List<User> employees = userService.getAllEmployees();
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
    public ResponseData addEmployee(@RequestBody CreateUserDTO createUserDTO) {
        User user = convertToUser(createUserDTO); // Using a helper method to convert DTO to User
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
    public ResponseData changeEmployee(@PathVariable Long id, @RequestBody CreateUserDTO updateUserDTO) {
        User updatedUser = convertToUser(updateUserDTO);
        Optional<User> updatedEmployee = userService.updateEmployee(id, updatedUser);
        return updatedEmployee.map(user -> ResponseData.builder()
                        .success(true)
                        .message(Utils.getMessage("employee_updated_success"))
                        .data(user)
                        .build())
                .orElseGet(() -> ResponseData.builder()
                        .success(false)
                        .message(Utils.getMessage("employee_not_found"))
                        .data(null)
                        .build());
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