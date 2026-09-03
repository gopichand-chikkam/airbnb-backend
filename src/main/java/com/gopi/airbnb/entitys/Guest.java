package com.gopi.airbnb.entitys;

import com.gopi.airbnb.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "guest")
    private List<BookingGuest>bookingGuestList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
