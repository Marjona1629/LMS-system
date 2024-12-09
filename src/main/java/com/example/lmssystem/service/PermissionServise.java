package com.example.lmssystem.service;

import com.example.lmssystem.entity.Permission;
import com.example.lmssystem.repository.PermissionRepository;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServise {

    private final PermissionRepository permittionRepository;

    private Permission getPermissionOrThrow(Long id) {
        return permittionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));
    }

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
        Permission permission = getPermissionOrThrow(id);
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .success(true)
                        .data(permission)
                        .message(Utils.getMessage("success"))
                        .build()
        );
    }

    public ResponseEntity<?> update(Long id, String name) {
        Permission permission = getPermissionOrThrow(id);

        if (permission.getName().equals(name)) {
            return ResponseEntity.status(400).body(
                    ResponseData.builder()
                            .success(false)
                            .data(null)
                            .message(Utils.getMessage("failed"))
                            .build()
            );
        }

        if (permittionRepository.existsByName(name)) {
            return ResponseEntity.status(400).body(
                    ResponseData.builder()
                            .success(false)
                            .data(null)
                            .message(Utils.getMessage("failed"))
                            .build()
            );
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
        if (permittionRepository.existsByName(name)) {
            return ResponseEntity.status(400).body(
                    ResponseData.builder()
                            .success(false)
                            .data(null)
                            .message(Utils.getMessage("failed"))
                            .build()
            );
        }

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