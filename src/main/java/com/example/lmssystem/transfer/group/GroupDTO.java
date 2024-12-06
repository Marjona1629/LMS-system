package com.example.lmssystem.transfer.group;

import com.example.lmssystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    private Long id;
    private String name;
    private Long teacherId;
    private Integer studentsCount;
    private String startTime;
    private List<Long> days;
}