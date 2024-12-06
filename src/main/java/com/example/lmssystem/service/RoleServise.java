package com.example.lmssystem.service;


import com.example.lmssystem.entity.Role;
import com.example.lmssystem.repository.PermissionRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.role_permission.RoleDTO;
import com.example.lmssystem.utils.Utils;
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

    public ResponseEntity<?> getRoleById( Long id) {
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(roleRepository.findById(id).orElseThrow())
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> createRole(String name, List<Long> permissions) {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(new ArrayList<>());
        for (Long permission : permissions) {
            try {
                role.getPermissions().add(permittionRepository.findById(permission).orElseThrow());
            }catch (Exception e){}
        }
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(roleRepository.save(role))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }


    public ResponseEntity<?> addPermission(RoleDTO role1) {
        System.out.println(role1);
        Role role = roleRepository.findById(role1.id()).orElseThrow();
        role.getPermissions().add(permittionRepository.findById(role1.permissionId()).orElseThrow());
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(roleRepository.save(role))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
