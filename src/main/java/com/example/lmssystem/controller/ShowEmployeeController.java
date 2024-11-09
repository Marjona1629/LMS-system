package com.example.lmssystem.controller;

import com.example.lmssystem.entity.User;
import com.example.lmssystem.service.BranchService;
import com.example.lmssystem.service.UserService;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.auth.CreateUserDTO;
import com.example.lmssystem.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workers")
public class ShowEmployeeController {

    @Autowired
    private UserService userService;

    @GetMapping("/archived")
    public ResponseEntity<ResponseData> getArchivedEmployees() {
        List<User> archivedUsers = userService.getArchivedEmployees();

        List<CreateUserDTO> archivedUserDTOs = archivedUsers.stream()
                .map(userService::mapToCreateUserDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("user.archived.retrieved_success"))
                .data(archivedUserDTOs)
                .build());
    }

    @GetMapping("/active-teachers")
    public ResponseEntity<ResponseData> getActiveTeachers() {
        List<User> activeTeachers = userService.getActiveTeachers();

        if (activeTeachers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.builder()
                            .success(false)
                            .message(Utils.getMessage("user.teachers.archived.not_found"))
                            .data(null)
                            .build());
        }
        List<CreateUserDTO> teacherDTOs = activeTeachers.stream()
                .map(userService::mapToCreateUserDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("user.teachers.active.retrieved_success"))
                .data(teacherDTOs)
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