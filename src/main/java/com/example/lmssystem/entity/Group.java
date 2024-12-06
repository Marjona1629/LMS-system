package com.example.lmssystem.entity;

import com.example.lmssystem.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups")
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private User teacher;
    private Timestamp startTime;
    private Double duration;
    private Status status;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Room room;
    @ManyToOne
    private Branch branch;
    @ManyToMany
    private List<User> students;
    @ManyToMany
    private List<Day> days;
}
