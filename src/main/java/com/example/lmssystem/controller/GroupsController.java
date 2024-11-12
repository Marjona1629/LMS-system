package com.example.lmssystem.controller;

import com.example.lmssystem.servise.GroupServise;
import com.example.lmssystem.trnasfer.group.CreateGroupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
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
}