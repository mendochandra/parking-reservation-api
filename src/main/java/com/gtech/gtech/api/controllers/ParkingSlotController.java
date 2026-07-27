package com.gtech.gtech.api.controllers;

import com.gtech.gtech.api.mappers.ParkingSlotMapper;
import com.gtech.gtech.api.request.ParkingSlotRequest;
import com.gtech.gtech.api.response.ParkingSlotResponse;
import com.gtech.gtech.service.parking.ParkingSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-slots")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;
    private final ParkingSlotMapper parkingSlotMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingSlotResponse create(@Valid @RequestBody ParkingSlotRequest request) {
        return parkingSlotMapper.toResponse(parkingSlotService.create(request));
    }

    @GetMapping("/{slotId}")
    public ParkingSlotResponse findById(@PathVariable String slotId) {

        return parkingSlotMapper.toResponse(parkingSlotService.findById(slotId));
    }

    @GetMapping("/lot/{lotId}")
    public List<ParkingSlotResponse> findByLot(@PathVariable String lotId) {

        return parkingSlotService.findByLot(lotId)
                .stream()
                .map(parkingSlotMapper::toResponse)
                .toList();
    }

    @PutMapping("/{slotId}")
    public ParkingSlotResponse update(@PathVariable String slotId, @Valid @RequestBody ParkingSlotRequest request) {

        return parkingSlotMapper.toResponse(parkingSlotService.update(slotId, request));
    }

    @DeleteMapping("/{slotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String slotId) {

        parkingSlotService.delete(slotId);
    }
}
