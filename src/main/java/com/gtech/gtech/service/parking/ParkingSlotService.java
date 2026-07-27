package com.gtech.gtech.service.parking;

import com.gtech.gtech.api.request.ParkingSlotRequest;
import com.gtech.gtech.domain.entity.ParkingSlot;

import java.util.List;

public interface ParkingSlotService {

    ParkingSlot create(ParkingSlotRequest request);

    ParkingSlot update(String slotId, ParkingSlotRequest request);

    ParkingSlot findById(String slotId);

    List<ParkingSlot> findByLot(String lotId);

    void delete(String slotId);


}
