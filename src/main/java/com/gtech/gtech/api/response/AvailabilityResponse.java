package com.gtech.gtech.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {

    private String slotId;

    private Integer floorLevel;

    private String vehicleType;

}
