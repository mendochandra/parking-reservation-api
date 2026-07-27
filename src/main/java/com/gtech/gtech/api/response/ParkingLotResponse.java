package com.gtech.gtech.api.response;

import com.gtech.gtech.domain.valueobject.OperatingHours;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotResponse {

    private String lotId;

    private String name;

    private String location;

    private OperatingHours operatingHours;

}
