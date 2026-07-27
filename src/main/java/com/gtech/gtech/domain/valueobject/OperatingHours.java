package com.gtech.gtech.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperatingHours {

    @NotNull
    private LocalTime openingTime;

    @NotNull
    private LocalTime closingTime;

}
