package com.gtech.gtech.api.request;

import com.gtech.gtech.domain.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReservationRequest {

    private String reservationId;

    private String lotId;

    private VehicleType vehicleType;

    private String licensePlate;

    private String customerId;

    private LocalDateTime plannedStartTime;

    private LocalDateTime plannedEndTime;

}
