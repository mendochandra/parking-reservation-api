package com.gtech.gtech.domain.entity;

import com.gtech.gtech.domain.enums.ReservationStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reservationId;

    @NotBlank
    private String lotId;

    @NotBlank
    private String slotId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @NotBlank
    private String licensePlate;

    @NotBlank
    private String customerId;

    @NotNull
    private LocalDateTime plannedStartTime;

    @NotNull
    private LocalDateTime plannedEndTime;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private boolean lateCancellation;

    private String reason;

}
