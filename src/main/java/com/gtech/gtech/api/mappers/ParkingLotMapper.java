package com.gtech.gtech.api.mappers;

import com.gtech.gtech.api.request.ParkingLotRequest;
import com.gtech.gtech.api.response.ParkingLotResponse;
import com.gtech.gtech.domain.entity.ParkingLot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ParkingLotMapper {

    @Mapping(target = "slots", ignore = true)
    ParkingLot toEntity(ParkingLotRequest request);

    ParkingLotResponse toResponse(ParkingLot parkingLot);

}

