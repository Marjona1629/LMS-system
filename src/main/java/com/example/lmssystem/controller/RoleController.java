package com.example.lmssystem.controller;

import com.example.lmssystem.servise.RoleServise;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServise roleServise;
    @GetMapping
    public ResponseEntity<?> getRole() {
        return roleServise.getAllRoles();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        return roleServise.getRoleById(id);
    }
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody String  name, List<Long> permissions) {
        return roleServise.createRole(name,permissions);
    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
//        roleServise.deleteRoleById(id);
//        return ResponseEntity.status(200).body(
//                ResponseData.builder()
//                        .success(true)
//                        .data(null)
//                        .message(Utils.getMessage("success"))
//                        .build()
//        );
//    }
}
