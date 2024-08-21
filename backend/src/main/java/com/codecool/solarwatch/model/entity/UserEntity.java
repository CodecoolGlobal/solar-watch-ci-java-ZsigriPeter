package com.codecool.solarwatch.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue
    private long id;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public UserEntity() {}

    public UserEntity(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

}
