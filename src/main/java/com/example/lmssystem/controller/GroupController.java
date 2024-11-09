package com.example.lmssystem.controller;

import com.example.lmssystem.entity.Group;
import com.example.lmssystem.service.GroupService;
import com.example.lmssystem.transfer.GroupDTO;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{teacherId}")
    public List<Group> getGroupsByTeacher(@PathVariable Long teacherId) {
        return groupService.getGroupsByTeacher(teacherId);
    }

    @GetMapping("/show-all")
    public ResponseEntity<ResponseData> showGroups(
            @RequestParam(required = false) Long teacherId) {
        List<GroupDTO> groups = groupService.findGroups(teacherId);

        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseData.builder()
                            .success(false)
                            .message(Utils.getMessage("user.groups.not_found"))
                            .data(null)
                            .build());
        }
        return ResponseEntity.ok(ResponseData.builder()
                .success(true)
                .message(Utils.getMessage("user.groups.retrieved_success"))
                .data(groups)
                .build());
    }
}