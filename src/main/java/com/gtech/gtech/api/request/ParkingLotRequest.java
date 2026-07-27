package com.gtech.gtech.api.request;

import com.gtech.gtech.domain.valueobject.OperatingHours;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotRequest {

    private String lotId;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    private OperatingHours operatingHours;

}
