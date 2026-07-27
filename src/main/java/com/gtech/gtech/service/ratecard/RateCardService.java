package com.gtech.gtech.service.ratecard;

import com.gtech.gtech.api.request.RateCardRequest;
import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.domain.enums.VehicleType;

import java.util.List;

public interface RateCardService {

    RateCard create(RateCardRequest request);

    RateCard update(String vehicleType, RateCardRequest request);

    RateCard findByVehicleType(VehicleType vehicleType);

    List<RateCard> findAll();

    void delete(VehicleType vehicleType);

}
