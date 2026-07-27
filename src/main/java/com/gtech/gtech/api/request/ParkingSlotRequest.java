package com.gtech.gtech.api.request;

import com.gtech.gtech.domain.enums.ParkingSlotStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotRequest {

    private String id;

    private String slotId;

    private String lotId;

    @NotNull
    private VehicleType vehicleType;

    private ParkingSlotStatus status;

    private int floorLevel;

}
