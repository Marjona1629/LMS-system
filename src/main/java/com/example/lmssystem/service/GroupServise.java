package com.example.lmssystem.service;

import com.example.lmssystem.entity.*;
import com.example.lmssystem.enums.Status;
import com.example.lmssystem.repository.*;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.transfer.group.CreateGroupDTO;
import com.example.lmssystem.transfer.group.GroupDTO;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        if (!timeCheck(groupDTO)) {
            return ResponseEntity.status(400).body(
                    ResponseData.builder()
                            .message(Utils.getMessage("failed"))
                            .data(null)
                            .success(false)
                            .build()
            );
        }
        String[] split = groupDTO.startTime().split(":");
        LocalTime localTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

        Group build = Group.builder()
                .course(courseRepository.findById(groupDTO.courseId()).orElseThrow())
                .duration(groupDTO.duration())
                .name(groupDTO.name())
                .room(roomRepository.findById(groupDTO.roomId()).orElseThrow())
                .branch(branchRepository.findById(groupDTO.branchId()).orElseThrow())
                .description(groupDTO.description())
                .status(Status.WAITING)
                .startTime(Timestamp.valueOf(LocalDateTime.of(0, 0, 0, localTime.getHour(), localTime.getMinute())))
                .build();
        User user = userRepository.findById(groupDTO.userId()).orElseThrow();
        for (Role role : user.getRole()) {
            if (role.getName().equals("TEACHER")) {
                build.setTeacher(user);
            }
        }

        return ResponseEntity.status(200).body(
                ResponseData.builder()
                        .message(Utils.getMessage("success"))
                        .data(groupRepository.save(build))
                        .success(true)
                        .build()
        );
    }

    private boolean timeCheck(CreateGroupDTO groupDTO) {
        String[] split = groupDTO.startTime().split(":");
        LocalTime startTime = LocalTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        List<Long> days = groupDTO.days();
        return groupRepository.findAll().stream()
                .filter(group ->{
                    for (Day day : group.getDays()) {
                        for (Long l : days) {
                            if(l==day.getId()){
                                return true;
                            }
                        }
                    }
                    return false;

                })
                .filter(group -> {
                    LocalTime groupStartTime = LocalTime.of(group.getStartTime().getHours(), group.getStartTime().getMinutes());
                    LocalTime groupEndTime = timeFormat(groupStartTime, group.getDuration());
                    LocalTime endTime = timeFormat(startTime, groupDTO.duration());
                    return isBetween(startTime, endTime, groupStartTime, groupEndTime);
                }).toList().isEmpty();

    }

    private boolean isBetween(LocalTime startTime,LocalTime endTime, LocalTime groupStartTime, LocalTime groupEndTime) {
        return startTime.isBefore(groupStartTime) && endTime.isBefore(groupStartTime) && startTime.isBefore(endTime) || startTime.isAfter(groupEndTime) && endTime.isAfter(groupEndTime) && startTime.isBefore(endTime);
    }

    private LocalTime timeFormat(LocalTime startTime, Double duration) {
        LocalTime localTime =LocalTime.of(startTime.getHour(), startTime.getMinute());
        String[] split = duration.toString().split(".");
        long hour=Long.parseLong(split[0]);
        long minut=Long.parseLong(split[1]);
        return localTime.plusHours(hour).plusMinutes(minut);
    }
    public List< GroupDTO> getAll(){
        return groupRepository.findAll().stream().map(this::groupToDTO).toList();
    }

    public List<GroupDTO> findGroups(Long teacherId) {
        List<GroupDTO> groupDTOS = new ArrayList<>();
        groupRepository.findByTeacher_Id(teacherId).forEach(group -> {
            groupDTOS.add(groupToDTO(group));
        });
        return groupDTOS;
    }
    private GroupDTO groupToDTO(Group group) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(group.getName());
        groupDTO.setId(group.getId());
        groupDTO.setDays(group.getDays().stream().map(Day::getId).toList());
        groupDTO.setStartTime(formatter.format(group.getStartTime()));
        groupDTO.setStudentsCount(group.getStudents().size());
        groupDTO.setTeacherId(group.getTeacher().getId());
        return groupDTO;
    }
}