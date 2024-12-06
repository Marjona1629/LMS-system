package com.example.lmssystem.controller.chala;

import com.example.lmssystem.service.GroupServise;

import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.group.CreateGroupDTO;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GroupsController {
    private final GroupServise groupServise;
    @GetMapping
    public ResponseEntity<?> getGroups() {
        return groupServise.getAllGroups();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable("id") Long id) {
        return groupServise.getGroupById(id);
    }
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupDTO group) {
        return groupServise.createGroup(group);
    }
    @GetMapping("/{teacherId}")
    public ResponseEntity<?> getGroupsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.status(200)
                .body(ResponseData.builder()
                        .success(true)
                        .message(Utils.getMessage("success"))
                        .data(groupServise.findGroups(teacherId))
                        .build());
    }

}