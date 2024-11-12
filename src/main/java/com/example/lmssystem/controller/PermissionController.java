package com.example.lmssystem.controller;

import com.example.lmssystem.servise.PermissionServise;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
public class PermissionController {
    private final PermissionServise permissionServise;
    @GetMapping
    public ResponseEntity<?> getPermissions() {
        return permissionServise.getAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPermissionById(@PathVariable Long id) {
       return permissionServise.getId(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Long id, @RequestBody String name ) {
        return permissionServise.update(id,name);
    }
    @PostMapping
    public ResponseEntity<?> createPermission( @RequestBody String name ) {
        return permissionServise.create(name);
    }
}
