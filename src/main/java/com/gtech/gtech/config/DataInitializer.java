package com.gtech.gtech.config;

import com.gtech.gtech.domain.entity.ParkingLot;
import com.gtech.gtech.domain.entity.ParkingSlot;
import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.domain.enums.ParkingSlotStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import com.gtech.gtech.repository.ParkingLotRepository;
import com.gtech.gtech.repository.ParkingSlotRepository;
import com.gtech.gtech.repository.RateCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final RateCardRepository rateCardRepository;

    @Override
    public void run(String... args) {

        if (parkingLotRepository.count() > 0) {
            return;
        }

        ParkingLot lot = new ParkingLot();
        lot.setLotId("LOT-JKT01");
        lot.setName("Jakarta Parking");
        lot.setLocation("Jakarta");

        parkingLotRepository.save(lot);

        ParkingSlot slot1 = new ParkingSlot();
        slot1.setSlotId("A-01");
        slot1.setParkingLot(lot);
        slot1.setVehicleType(VehicleType.CAR);
        slot1.setFloorLevel(0);
        slot1.setStatus(ParkingSlotStatus.AVAILABLE);

        ParkingSlot slot2 = new ParkingSlot();
        slot2.setSlotId("A-02");
        slot2.setParkingLot(lot);
        slot2.setVehicleType(VehicleType.CAR);
        slot2.setFloorLevel(0);
        slot2.setStatus(ParkingSlotStatus.AVAILABLE);

        parkingSlotRepository.save(slot1);
        parkingSlotRepository.save(slot2);

        RateCard car = new RateCard();
        car.setVehicleType(VehicleType.CAR);
        car.setHourlyRate(new BigDecimal("5000"));
        car.setDailyCap(new BigDecimal("50000"));
        car.setOvernightSurcharge(new BigDecimal("20000"));
        car.setGracePeriodMinutes(15);

        rateCardRepository.save(car);
    }
}
