package com.example.lmssystem.entity;

import com.example.lmssystem.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private User techer;
    private LocalTime startTime;
    private Double duration;
    private String status;
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
