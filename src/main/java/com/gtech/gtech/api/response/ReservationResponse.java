package com.gtech.gtech.api.response;

import com.gtech.gtech.domain.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    private String reservationId;

    private String lotId;

    private String slotId;

    private Integer floorLevel;

    private String customerId;

    private String licensePlate;

    private String vehicleType;

    private ReservationStatus status;

    private Boolean lateCancellation;

    private LocalDateTime plannedStartTime;

    private LocalDateTime plannedEndTime;

    private LocalDateTime actualStartTime;

    private LocalDateTime actualEndTime;

}
