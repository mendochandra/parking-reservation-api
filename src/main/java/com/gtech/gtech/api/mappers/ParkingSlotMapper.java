package com.gtech.gtech.api.mappers;

import com.gtech.gtech.api.request.ParkingSlotRequest;
import com.gtech.gtech.api.response.ParkingSlotResponse;
import com.gtech.gtech.domain.entity.ParkingSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingSlotMapper {

    @Mapping(target = "parkingLot", ignore = true)
    ParkingSlot toEntity(ParkingSlotRequest request);

    @Mapping(target = "lotId",
            source = "parkingLot.lotId")
    ParkingSlotResponse toResponse(ParkingSlot slot);

}
