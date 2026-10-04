package com.gopi.airbnb.entitys;
import com.gopi.airbnb.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private Long id;
    private LocalDate check_in;
    private LocalDate check_out;
   private LocalDateTime paymentWindowStarted;
   private LocalDateTime paymentWindowExpires;
   private Integer totalGuest;
   @Enumerated(EnumType.STRING)
   private ReservationStatus reservationStatus;
   @ManyToOne
   @JoinColumn(name = "user_id")
   private User user;
   @ManyToOne
   @JoinColumn(name = "room_id")
   private Room room;
}
