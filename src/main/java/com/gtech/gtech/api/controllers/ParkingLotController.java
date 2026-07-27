package com.gtech.gtech.api.controllers;

import com.gtech.gtech.api.mappers.ParkingLotMapper;
import com.gtech.gtech.api.request.ParkingLotRequest;
import com.gtech.gtech.api.response.ParkingLotResponse;
import com.gtech.gtech.service.parking.ParkingLotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;
    private final ParkingLotMapper parkingLotMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingLotResponse create(@Valid @RequestBody ParkingLotRequest request) {
        return parkingLotMapper.toResponse(parkingLotService.create(request));
    }

    @GetMapping
    public List<ParkingLotResponse> findAll() {

        return parkingLotService.findAll()
                .stream()
                .map(parkingLotMapper::toResponse)
                .toList();
    }

    @GetMapping("/{lotId}")
    public ParkingLotResponse findById(@PathVariable String lotId) {
        return parkingLotMapper.toResponse(parkingLotService.findById(lotId));
    }

    @PutMapping("/{lotId}")
    public ParkingLotResponse update(@PathVariable String lotId, @Valid @RequestBody ParkingLotRequest request) {
        return parkingLotMapper.toResponse(parkingLotService.update(lotId, request));
    }

    @DeleteMapping("/{lotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String lotId) {
        parkingLotService.delete(lotId);
    }

}
