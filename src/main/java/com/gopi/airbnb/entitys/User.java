package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ElementCollection
    private List<Role>roles;
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Guest>guestList;

    @OneToMany(mappedBy = "user")
    private List<Booking>bookings;

}
