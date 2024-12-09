package com.example.lmssystem.service;

import com.example.lmssystem.entity.Role;
import com.example.lmssystem.exception.UserServiceException;
import com.example.lmssystem.repository.PermissionRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.role_permission.RoleDTO;
import com.example.lmssystem.utils.Utils;
import com.example.lmssystem.exception.PermissionNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServise {
    private final RoleRepository roleRepository;
    private final PermissionRepository permittionRepository;

    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(roleRepository.findAll())
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(role)
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> createRole(String name, List<Long> permissions) {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(new ArrayList<>());

        for (Long permission : permissions) {
            permittionRepository.findById(permission)
                    .orElseThrow(() -> new PermissionNotFoundException("Permission not found with id: " + permission));
            role.getPermissions().add(permittionRepository.findById(permission).get());
        }

        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(roleRepository.save(role))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> addPermission(RoleDTO roleDTO) {
        Role role = roleRepository.findById(roleDTO.id()).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleDTO.id()));
        role.getPermissions().add(permittionRepository.findById(roleDTO.permissionId())
                .orElseThrow(() -> new PermissionNotFoundException("Permission not found with id: " + roleDTO.permissionId())));

        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(roleRepository.save(role))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new UserServiceException("Role not found: " + roleName));
    }


    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}