package com.example.lmssystem.controller.bitganlar;

import com.example.lmssystem.entity.User;
import com.example.lmssystem.service.UserService;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.auth.CreateEmployeeDTO;
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
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private UserService userService;
    public EmployeeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseData showEmployees() {
        List<CreateUserDTO> employees = userService.getAllEmployees().stream()
                .map(dto -> new CreateUserDTO(
                        dto.firstName(),
                        dto.lastName(),
                        dto.phoneNumber(),
                        dto.gender(),
                        dto.birthDate(),
                        dto.branchNames(),
                        dto.roles()
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
    public ResponseData addEmployee(@RequestBody CreateEmployeeDTO createEmployeeDTO) {
        User user = userService.convertToUser(createEmployeeDTO);
        User addedEmployee = userService.addEmployee(user);

        return ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("employee_added_success"))
                .data(addedEmployee)
                .build();
    }

    @GetMapping("/search")
    public List<CreateUserDTO> searchEmployees(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber) {
        return userService.searchEmployees(id, firstName, lastName, phoneNumber);
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
        User updatedUser = userService.convertToUser(updateUserDTO);
        User updatedEmployee = userService.updateEmployee(id, updatedUser);

        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("employee_updated_success"))
                .data(updatedEmployee)
                .build());
    }

    @GetMapping("/other-active")
    public ResponseEntity<ResponseData> getOtherActiveEmployees() {
        List<User> activeEmployees = userService.getOtherActiveEmployees();

        if (!activeEmployees.isEmpty()) {
            List<CreateUserDTO> employeesDTO = activeEmployees.stream()
                    .map(userService::mapToCreateUserDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ResponseData.builder()
                    .success(true)
                    .message(Utils.getMessage("user.employees.active.retrieved_success"))
                    .data(employeesDTO)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.builder()
                            .success(false)
                            .message(Utils.getMessage("user.employees.active.not_found"))
                            .data(null)
                            .build());
        }
    }
}