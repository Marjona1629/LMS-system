package com.example.lmssystem.entity;

import com.example.lmssystem.entity.*;
import com.example.lmssystem.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private User teacher;
    private LocalTime startTime;
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