package com.example.lmssystem.servise;

import com.example.lmssystem.entity.Course;
import com.example.lmssystem.entity.Group;
import com.example.lmssystem.enums.Status;
import com.example.lmssystem.repository.*;
import com.example.lmssystem.trnasfer.ResponseData;
import com.example.lmssystem.trnasfer.group.CreateGroupDTO;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GroupServise {
    private final GroupRepository groupRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final DayRepository dayRepository;
    private final CourseRepository courseRepository;
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.status(200).body(ResponseData
                .builder()
                .success(true)
                .data(groupRepository.findAll())
                .message("Success")
                .build()
        );
    }

    public ResponseEntity<?> getGroupById(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new RuntimeException(Utils.getMessage("employee_not_found", id))
        );
        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .message(Utils.getMessage("employee_found", id))
                        .data(group)
                        .success(true)
                        .build()
        );
    }

    public ResponseEntity<?> createGroup(CreateGroupDTO groupDTO) {
        String[] split = groupDTO.startTime().split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 0);
        Course course = courseRepository.findById(groupDTO.courseId()).orElseThrow();
        Group.builder()
                .branch(branchRepository.findById(groupDTO.branchId()).orElseThrow())
                .name(groupDTO.name())
                .room(roomRepository.findById(groupDTO.roomId()).orElseThrow())
                .course(course)
                .duration(course.getDuration())
                .description(groupDTO.description())
                .teacher(userRepository.findById(groupDTO.userId()).orElseThrow())
                .status(Status.WAITING)
                .students(new ArrayList<>())
                .build();



    }
}