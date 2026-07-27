package com.gtech.gtech.repository;

import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.domain.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateCardRepository extends JpaRepository<RateCard, VehicleType> {

    Optional<RateCard> findByVehicleType(
            VehicleType vehicleType
    );

}
