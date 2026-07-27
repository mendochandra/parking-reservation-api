package com.gtech.gtech.domain.entity;

import com.gtech.gtech.domain.valueobject.OperatingHours;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParkingLot {

    @Id
    private String lotId;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @Valid
    @Embedded
    private OperatingHours operatingHours;

    @OneToMany(
            mappedBy = "parkingLot",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ParkingSlot> slots = new ArrayList<>();

}
