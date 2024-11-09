package com.example.lmssystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data

public class Branch {
    @Id
    private Long id;
    private String name;
    @OneToOne
    private Location location;
    private boolean active;

    public boolean isValid() {
        return active;
    }
}