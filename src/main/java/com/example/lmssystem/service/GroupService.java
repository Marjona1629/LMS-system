package com.example.lmssystem.service;

import com.example.lmssystem.entity.Group;
import com.example.lmssystem.repository.GroupRepository;
import com.example.lmssystem.transfer.GroupDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getGroupsByTeacher(Long teacherId) {
        return groupRepository.findByTeacherId(teacherId);
    }

    public List<GroupDTO> findGroups(Long teacherId) {
        List<Group> groups;

        if (teacherId != null) {
            groups = groupRepository.findByTeacherId(teacherId);
        } else {
            groups = groupRepository.findAll();
        }
        return groups.stream()
                .map(this::toGroupDTO)
                .collect(Collectors.toList());
    }


    private GroupDTO toGroupDTO(Group group) {
        if (group == null) {
            return null;
        }
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        return groupDTO;
    }
}