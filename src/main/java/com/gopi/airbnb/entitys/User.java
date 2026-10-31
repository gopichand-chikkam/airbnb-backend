package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ElementCollection
    private List<Role>roles;
    private String name;

    @Column(nullable = false,unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Guest>guestList;

    @OneToMany(mappedBy = "user")
    private List<Booking>bookings;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
