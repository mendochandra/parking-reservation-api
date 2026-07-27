package com.gtech.gtech.domain.entity;

import com.gtech.gtech.domain.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RateCard {

    @Id
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @NotNull
    //rate per hour
    private BigDecimal hourlyRate;

    @NotNull
    //maximum charge per calendar day
    private BigDecimal dailyCap;

    @NotNull
    //flat fee if vehicle stays past midnight
    private BigDecimal overnightSurcharge;

    //free minutes at start;
    private int gracePeriodMinutes = 15;

}
