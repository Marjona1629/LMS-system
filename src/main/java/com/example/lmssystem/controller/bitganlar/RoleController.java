package com.example.lmssystem.controller.bitganlar;

import com.example.lmssystem.service.RoleServise;
import com.example.lmssystem.transfer.role_permission.RoleDTO;
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
    @PutMapping
    public ResponseEntity<?> rolePlusPermission(@RequestBody RoleDTO role) {
        return roleServise.addPermission(role);
    }

}
