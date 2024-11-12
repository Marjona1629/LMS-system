package com.example.lmssystem.servise;

import com.example.lmssystem.entity.Permission;
import com.example.lmssystem.entity.Role;
import com.example.lmssystem.repository.PermittionRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.trnasfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServise {
    private final PermittionRepository permittionRepository;
    private final RoleRepository roleRepository;

    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(permittionRepository.findAll())
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> getId(Long id) {
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(permittionRepository.findById(id).orElseThrow())
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> update(Long id, String name) {
        Permission permission = permittionRepository.findById(id).orElseThrow();
        if(permission.getName().equals(name)) {
            return ResponseEntity.status(401).body(
                    ResponseData.builder()
                            .success(true)
                            .data(null)
                            .message(Utils.getMessage("failed"))
                            .build()
            );
        }

        for (Permission permission1 : permittionRepository.findAll()) {
            if(permission1.getName().equals(name)) {
                return ResponseEntity.status(401).body(
                        ResponseData.builder()
                                .success(true)
                                .data(null)
                                .message(Utils.getMessage("failed"))
                                .build()
                );
            }
        }
        permission.setName(name);
        permittionRepository.save(permission);
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(null)
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> create(String name) {
        Permission permission = new Permission();
        permission.setName(name);
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(permittionRepository.save(permission))
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }
}
