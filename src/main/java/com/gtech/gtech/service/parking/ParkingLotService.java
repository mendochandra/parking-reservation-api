package com.gtech.gtech.service.parking;

import com.gtech.gtech.api.request.ParkingLotRequest;
import com.gtech.gtech.domain.entity.ParkingLot;

import java.util.List;

public interface ParkingLotService {

    ParkingLot create(ParkingLotRequest request);

    ParkingLot update(String lotId, ParkingLotRequest request);

    ParkingLot findById(String lotId);

    List<ParkingLot> findAll();

    void delete(String lotId);

}
