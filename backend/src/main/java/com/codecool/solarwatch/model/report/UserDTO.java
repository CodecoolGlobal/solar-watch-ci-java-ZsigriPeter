package com.codecool.solarwatch.model.report;

import com.codecool.solarwatch.model.entity.Role;

import java.util.Set;

public record UserDTO(String username,String password,Set<Role>roles) {
}
