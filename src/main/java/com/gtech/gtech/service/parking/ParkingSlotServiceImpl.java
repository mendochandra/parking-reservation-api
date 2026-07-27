package com.gtech.gtech.service.parking;

import com.gtech.gtech.api.mappers.ParkingSlotMapper;
import com.gtech.gtech.api.request.ParkingSlotRequest;
import com.gtech.gtech.domain.entity.ParkingLot;
import com.gtech.gtech.domain.entity.ParkingSlot;
import com.gtech.gtech.domain.enums.ParkingSlotStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import com.gtech.gtech.exception.ReservationNotFoundException;
import com.gtech.gtech.repository.ParkingLotRepository;
import com.gtech.gtech.repository.ParkingSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingSlotServiceImpl implements ParkingSlotService{

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSlotMapper parkingSlotMapper;

    @Override
    public ParkingSlot create(ParkingSlotRequest request) {

        ParkingLot lot = parkingLotRepository.findById(request.getLotId())
                .orElseThrow(() ->
                        new ReservationNotFoundException("Parking lot not found"));

        ParkingSlot slot = parkingSlotMapper.toEntity(request);;
        slot.setSlotId(generateSlotId(request.getLotId(),request.getVehicleType()));
        slot.setStatus(ParkingSlotStatus.AVAILABLE);
        slot.setParkingLot(lot);
        return parkingSlotRepository.save(slot);
    }

    @Override
    public ParkingSlot update(String slotId, ParkingSlotRequest request) {

        ParkingSlot slot = parkingSlotRepository.findById(slotId)
                .orElseThrow(() ->
                        new ReservationNotFoundException("Parking slot not found"));

        slot.setFloorLevel(request.getFloorLevel());
        slot.setStatus(request.getStatus());
        slot.setVehicleType(request.getVehicleType());

        return parkingSlotRepository.save(slot);
    }

    @Override
    public ParkingSlot findById(String slotId) {

        return parkingSlotRepository.findById(slotId)
                .orElseThrow(() ->
                        new ReservationNotFoundException("Parking slot not found"));
    }

    @Override
    public List<ParkingSlot> findByLot(String lotId) {

        return parkingSlotRepository.findByParkingLotLotId(lotId);
    }

    @Override
    public void delete(String slotId) {

        parkingSlotRepository.deleteById(slotId);
    }

    public String generateSlotId(String lotId, VehicleType vehicleType) {

        String prefix = switch (vehicleType) {
            case CAR -> "A";
            case MOTORCYCLE -> "B";
            case TRUCK -> "C";
        };

        List<ParkingSlot> slots =
                parkingSlotRepository
                        .findByParkingLotLotIdAndVehicleTypeOrderBySlotIdAsc(
                                lotId,
                                vehicleType);

        if (slots.isEmpty()) {
            return prefix + "-01";
        }

        String lastSlotId =
                slots.get(slots.size() - 1).getSlotId();

        int lastNumber =
                Integer.parseInt(lastSlotId.substring(2));

        return "%s-%02d".formatted(
                prefix,
                lastNumber + 1);
    }
}
