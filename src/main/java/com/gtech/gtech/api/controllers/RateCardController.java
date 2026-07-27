package com.gtech.gtech.api.controllers;

import com.gtech.gtech.api.mappers.RateCardMapper;
import com.gtech.gtech.api.request.RateCardRequest;
import com.gtech.gtech.api.response.RateCardResponse;
import com.gtech.gtech.domain.enums.VehicleType;
import com.gtech.gtech.service.ratecard.RateCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rate-cards")
@RequiredArgsConstructor
public class RateCardController {

    private final RateCardService rateCardService;
    private final RateCardMapper rateCardMapper;

    @PostMapping
    public ResponseEntity<RateCardResponse> create(@Valid @RequestBody RateCardRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rateCardMapper.toResponse(
                        rateCardService.create(request)));
    }

    @GetMapping
    public ResponseEntity<List<RateCardResponse>> findAll() {

        return ResponseEntity.ok(
                rateCardService.findAll()
                        .stream()
                        .map(rateCardMapper::toResponse)
                        .toList());
    }

    @GetMapping("/{vehicleType}")
    public ResponseEntity<RateCardResponse> findByVehicleType(@PathVariable VehicleType vehicleType) {

        return ResponseEntity.ok(
                rateCardMapper.toResponse(
                        rateCardService.findByVehicleType(vehicleType)));
    }

    @PutMapping("/{vehicleType}")
    public ResponseEntity<RateCardResponse> update(@PathVariable String vehicleType, @Valid @RequestBody RateCardRequest request) {

        return ResponseEntity.ok(
                rateCardMapper.toResponse(
                        rateCardService.update(vehicleType, request)));
    }

    @DeleteMapping("/{vehicleType}")
    public ResponseEntity<Void> delete(@PathVariable VehicleType vehicleType) {

        rateCardService.delete(vehicleType);
        return ResponseEntity.noContent().build();

    }

}
