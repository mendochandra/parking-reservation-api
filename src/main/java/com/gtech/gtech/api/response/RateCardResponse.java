package com.gtech.gtech.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateCardResponse {

    private String vehicleType;

    private String hourlyRate;

    private String dailyCap;

    private String overnightSurcharge;

    private Integer gracePeriodMinutes;
}
