package com.gtech.gtech.service.parking;

import com.gtech.gtech.api.mappers.ParkingLotMapper;
import com.gtech.gtech.api.request.ParkingLotRequest;
import com.gtech.gtech.domain.entity.ParkingLot;
import com.gtech.gtech.exception.ParkingSlotUnavailableException;
import com.gtech.gtech.exception.ReservationNotFoundException;
import com.gtech.gtech.repository.ParkingLotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingLotServiceImpl implements ParkingLotService{


    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotMapper parkingLotMapper;

    @Override
    public ParkingLot create(ParkingLotRequest request) {

        ParkingLot parkingLot = parkingLotMapper.toEntity(request);
        parkingLot.setLotId(generateLotId(request.getLocation()));
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public ParkingLot update(String lotId, ParkingLotRequest request) {

        ParkingLot parkingLot = parkingLotRepository.findById(lotId)
                .orElseThrow(() ->
                        new ReservationNotFoundException("Parking lot not found"));

        parkingLot.setName(request.getName());
        parkingLot.setLocation(request.getLocation());
        parkingLot.setOperatingHours(request.getOperatingHours());

        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public ParkingLot findById(String lotId) {

        return parkingLotRepository.findById(lotId)
                .orElseThrow(() ->
                        new ParkingSlotUnavailableException("Parking lot not found"));
    }

    @Override
    public List<ParkingLot> findAll() {

        return parkingLotRepository.findAll();
    }

    @Override
    public void delete(String lotId) {

        parkingLotRepository.deleteById(lotId);
    }

    private String generateLotId(String location) {

        String prefix = location.substring(0, 3)
                .toUpperCase();

        List<ParkingLot> lots =
                parkingLotRepository.findByLotIdStartingWith("LOT-" + prefix);

        int next = lots.size() + 1;

        return String.format("LOT-%s%03d", prefix, next);
    }
}
