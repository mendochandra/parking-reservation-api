package com.gtech.gtech.service.ratecard;

import com.gtech.gtech.api.mappers.RateCardMapper;
import com.gtech.gtech.api.request.RateCardRequest;
import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.domain.enums.VehicleType;
import com.gtech.gtech.repository.RateCardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RateCardServiceImpl implements RateCardService {

    private final RateCardRepository rateCardRepository;
    private final RateCardMapper rateCardMapper;

    @Override
    public RateCard create(RateCardRequest request) {

        if (rateCardRepository.existsById(request.getVehicleType())) {
            throw new IllegalArgumentException("Rate card already exists");
        }

        RateCard rateCard = rateCardMapper.toEntity(request);
        return rateCardRepository.save(rateCard);

    }

    @Override
    public RateCard update(String vehicleType, RateCardRequest request) {

        VehicleType type = VehicleType.valueOf(vehicleType);

        RateCard rateCard =
                rateCardRepository.findById(type)
                        .orElseThrow(() ->
                                new RuntimeException("Rate card not found"));

        rateCard.setHourlyRate(request.getHourlyRate());
        rateCard.setDailyCap(request.getDailyCap());
        rateCard.setOvernightSurcharge(request.getOvernightSurcharge());
        rateCard.setGracePeriodMinutes(request.getGracePeriodMinutes());

        return rateCardRepository.save(rateCard);
    }

    @Override
    public RateCard findByVehicleType(VehicleType vehicleType) {

        return rateCardRepository.findById(vehicleType)
                .orElseThrow(() ->
                        new RuntimeException("Rate card not found"));
    }

    @Override
    public List<RateCard> findAll() {

        return rateCardRepository.findAll();
    }

    @Override
    public void delete(VehicleType vehicleType) {

        rateCardRepository.deleteById(vehicleType);
    }

}
