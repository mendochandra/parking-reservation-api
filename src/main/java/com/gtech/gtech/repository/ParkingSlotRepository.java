package com.gtech.gtech.repository;

import com.gtech.gtech.domain.entity.ParkingSlot;
import com.gtech.gtech.domain.enums.VehicleType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, String> {

    List<ParkingSlot> findByParkingLotLotId(String lotId);

    List<ParkingSlot> findByParkingLotLotIdAndVehicleTypeOrderBySlotIdAsc(
            String lotId,
            VehicleType vehicleType);

    List<ParkingSlot> findByParkingLotLotIdAndVehicleTypeOrderByFloorLevelAscSlotIdAsc(
            String lotId,
            VehicleType vehicleType);

    Optional<ParkingSlot> findByParkingLotLotIdAndSlotId(
            String lotId,
            String slotId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from ParkingSlot s
        where s.parkingLot.lotId = :lotId
          and s.slotId = :slotId
    """)
    Optional<ParkingSlot> lockSlot(
            @Param("lotId") String lotId,
            @Param("slotId") String slotId);

}
