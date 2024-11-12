package com.example.lmssystem.servise;

import com.example.lmssystem.entity.Role;
import com.example.lmssystem.repository.PermittionRepository;
import com.example.lmssystem.repository.RoleRepository;
import com.example.lmssystem.trnasfer.ResponseData;
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
    private final PermittionRepository permittionRepository;
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

    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
    }
}
