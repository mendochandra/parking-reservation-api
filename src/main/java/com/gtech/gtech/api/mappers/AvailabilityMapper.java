package com.gtech.gtech.api.mappers;

import com.gtech.gtech.api.response.AvailabilityResponse;
import com.gtech.gtech.domain.entity.ParkingSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {

    @Mapping(target = "vehicleType",
            expression = "java(slot.getVehicleType().name())")
    AvailabilityResponse toResponse(ParkingSlot slot);

}
