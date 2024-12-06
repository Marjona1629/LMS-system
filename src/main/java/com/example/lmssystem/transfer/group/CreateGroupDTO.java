package com.example.lmssystem.transfer.group;

import java.util.List;

public record CreateGroupDTO(String name, String description, Long teacherID, String startTime, Long courseId, Long roomId, Long branchId, Long userId, List<Long> days,Double duration) {

}
