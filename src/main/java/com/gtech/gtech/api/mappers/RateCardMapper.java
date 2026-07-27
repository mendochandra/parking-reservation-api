package com.gtech.gtech.api.mappers;

import com.gtech.gtech.api.request.RateCardRequest;
import com.gtech.gtech.api.response.RateCardResponse;
import com.gtech.gtech.domain.entity.RateCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RateCardMapper {

    RateCard toEntity(RateCardRequest request);

    @Mapping(target = "vehicleType",
            expression = "java(rateCard.getVehicleType().name())")

    @Mapping(target = "hourlyRate",
            expression = "java(rateCard.getHourlyRate().toPlainString())")

    @Mapping(target = "dailyCap",
            expression = "java(rateCard.getDailyCap().toPlainString())")

    @Mapping(target = "overnightSurcharge",
            expression = "java(rateCard.getOvernightSurcharge().toPlainString())")
    RateCardResponse toResponse(RateCard rateCard);

}
