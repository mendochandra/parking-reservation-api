package com.gtech.gtech.api.request;

import com.gtech.gtech.domain.enums.VehicleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateCardRequest {

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    private BigDecimal hourlyRate;

    @NotNull
    private BigDecimal dailyCap;

    @NotNull
    private BigDecimal overnightSurcharge;

    private Integer gracePeriodMinutes;

}
