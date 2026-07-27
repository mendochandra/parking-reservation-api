package com.gtech.gtech.api.mappers;


import com.gtech.gtech.api.request.ReservationRequest;
import com.gtech.gtech.api.response.ReservationResponse;
import com.gtech.gtech.domain.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationResponse toResponse(Reservation reservation);
    Reservation toEntity(ReservationRequest request);

}
