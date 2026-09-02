package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private Long id;
    private List<Role>roles;
    private String name;
    private String email;
    private String password;
}
