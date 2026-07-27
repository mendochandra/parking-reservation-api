package com.gtech.gtech.domain.entity;

import com.gtech.gtech.domain.enums.ParkingSlotStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String slotId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ParkingSlotStatus status;

    @Min(0)
    private int floorLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private ParkingLot parkingLot;

}
