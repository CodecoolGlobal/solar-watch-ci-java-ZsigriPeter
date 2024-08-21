package com.codecool.solarwatch.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    @Getter
    private String name;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<UserEntity> users;

    public Role() {}
    public Role(String name) {
        this.name = name;
    }
}