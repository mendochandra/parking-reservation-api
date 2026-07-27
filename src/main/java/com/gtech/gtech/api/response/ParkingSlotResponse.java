package com.gtech.gtech.api.response;

import com.gtech.gtech.domain.enums.ParkingSlotStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotResponse {

    private String id;
    private String slotId;

    private String lotId;

    private VehicleType vehicleType;

    private ParkingSlotStatus status;

    private Integer floorLevel;

}
