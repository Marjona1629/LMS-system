package com.example.lmssystem.controller;

import com.example.lmssystem.entity.User;
import com.example.lmssystem.servise.UserService;
import com.example.lmssystem.trnasfer.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseData showEmployees() {
        List<User> employees = userService.getAllEmployees();
        return ResponseData.builder()
                .success(true)
                .message("Employees retrieved successfully")
                .data(employees)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData showEmployee(@PathVariable Long id) {
        Optional<User> employee = userService.getEmployeeById(id);
        if (employee.isPresent()) {
            return ResponseData.builder()
                    .success(true)
                    .message("Employee retrieved successfully")
                    .data(employee.get())
                    .build();
        }
        return ResponseData.builder()
                .success(false)
                .message("Employee not found")
                .data(null)
                .build();
    }

    @PostMapping
    public ResponseData addEmployee(@RequestBody User employee) {
        User addedEmployee = userService.addEmployee(employee);
        return ResponseData.builder()
                .success(true)
                .message("Employee added successfully")
                .data(addedEmployee)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseData deleteEmployee(@PathVariable Long id) {
        boolean isDeleted = userService.softDeleteEmployee(id);
        if (isDeleted) {
            return ResponseData.builder()
                    .success(true)
                    .message("Employee deleted successfully")
                    .data(null)
                    .build();
        }
        return ResponseData.builder()
                .success(false)
                .message("Employee not found")
                .data(null)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseData changeEmployee(@PathVariable Long id, @RequestBody User employee) {
        Optional<User> updatedEmployee = userService.updateEmployee(id, employee);
        return updatedEmployee.map(user -> ResponseData.builder()
                        .success(true)
                        .message("Employee updated successfully")
                        .data(user)
                        .build())
                .orElseGet(() -> ResponseData.builder()
                        .success(false)
                        .message("Employee not found")
                        .data(null)
                        .build());
    }
}