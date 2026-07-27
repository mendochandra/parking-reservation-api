package com.gtech.gtech.api.controllers;

import com.gtech.gtech.api.mappers.AvailabilityMapper;
import com.gtech.gtech.api.mappers.ParkingInvoiceMapper;
import com.gtech.gtech.api.mappers.ReservationMapper;
import com.gtech.gtech.api.request.*;
import com.gtech.gtech.api.response.AvailabilityResponse;
import com.gtech.gtech.api.response.CheckOutResponse;
import com.gtech.gtech.api.response.ParkingInvoiceResponse;
import com.gtech.gtech.api.response.ReservationResponse;
import com.gtech.gtech.domain.entity.ParkingInvoice;
import com.gtech.gtech.domain.entity.ParkingSlot;
import com.gtech.gtech.domain.entity.Reservation;

import com.gtech.gtech.domain.enums.VehicleType;
import com.gtech.gtech.exception.ParkingSlotUnavailableException;
import com.gtech.gtech.exception.ReservationNotFoundException;
import com.gtech.gtech.repository.ParkingInvoiceRepository;
import com.gtech.gtech.repository.ParkingSlotRepository;
import com.gtech.gtech.service.parking.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final ParkingInvoiceMapper parkingInvoiceMapper;

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingInvoiceRepository parkingInvoiceRepository;
    private final AvailabilityMapper availabilityMapper;

    @GetMapping("/{lotId}/availability")
    public ResponseEntity<List<AvailabilityResponse>> getAvailableSlots(
            @PathVariable String lotId,
            @RequestParam VehicleType vehicleType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime) {

        List<AvailabilityResponse> responses =
                reservationService.getAvailableSlots(
                                lotId,
                                vehicleType,
                                startTime,
                                endTime)
                        .stream()
                        .map(availabilityMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {

        Reservation reservation = reservationService.createReservation(request);

        ParkingSlot slot = parkingSlotRepository
                .findByParkingLotLotIdAndSlotId(
                        reservation.getLotId(),
                        reservation.getSlotId())
                .orElseThrow(() ->
                        new ParkingSlotUnavailableException(
                                "Parking slot not found."));

        ReservationResponse response = reservationMapper.toResponse(reservation);
        response.setFloorLevel(slot.getFloorLevel());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/reservations/{reservationId}/check-in")
    public ResponseEntity<ReservationResponse> checkIn(@PathVariable String reservationId, @Valid @RequestBody CheckInRequest request) {

        Reservation reservation =
                reservationService.checkIn(
                        reservationId,
                        request.getActualStartTime());

        ParkingSlot slot =
                parkingSlotRepository
                        .findByParkingLotLotIdAndSlotId(
                                reservation.getLotId(),
                                reservation.getSlotId())
                        .orElseThrow(() ->
                                new ParkingSlotUnavailableException(
                                        "Parking slot not found."));

        ReservationResponse response =
                reservationMapper.toResponse(reservation);

        response.setFloorLevel(slot.getFloorLevel());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservations/{reservationId}/check-out")
    public ResponseEntity<CheckOutResponse> checkOut(@PathVariable String reservationId, @Valid @RequestBody CheckOutRequest request) {

        Reservation reservation =
                reservationService.checkOut(
                        reservationId,
                        request.getActualEndTime());

        ParkingSlot slot =
                parkingSlotRepository
                        .findByParkingLotLotIdAndSlotId(
                                reservation.getLotId(),
                                reservation.getSlotId())
                        .orElseThrow(() ->
                                new ParkingSlotUnavailableException(
                                        "Parking slot not found."));

        ParkingInvoice invoice =
                parkingInvoiceRepository
                        .findByReservationId(reservationId)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        "Invoice not found."));

        ReservationResponse reservationResponse =
                reservationMapper.toResponse(reservation);

        reservationResponse.setFloorLevel(slot.getFloorLevel());

        ParkingInvoiceResponse invoiceResponse =
                parkingInvoiceMapper.toResponse(invoice);

        CheckOutResponse response = new CheckOutResponse();
        response.setReservation(reservationResponse);
        response.setInvoice(invoiceResponse);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable String reservationId, @Valid @RequestBody CancelReservationRequest request) {

        Reservation reservation =
                reservationService.cancelReservation(
                        reservationId,
                        request.getReason());

        ParkingSlot slot =
                parkingSlotRepository
                        .findByParkingLotLotIdAndSlotId(
                                reservation.getLotId(),
                                reservation.getSlotId())
                        .orElseThrow(() ->
                                new ParkingSlotUnavailableException(
                                        "Parking slot not found."));

        ReservationResponse response =
                reservationMapper.toResponse(reservation);

        response.setFloorLevel(slot.getFloorLevel());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/reservations/{reservationId}/extend")
    public ResponseEntity<ReservationResponse> extendReservation(@PathVariable String reservationId, @Valid @RequestBody ExtendReservationRequest request) {

        Reservation reservation =
                reservationService.extendReservation(
                        reservationId,
                        request.getNewEndTime());

        ParkingSlot slot =
                parkingSlotRepository
                        .findByParkingLotLotIdAndSlotId(
                                reservation.getLotId(),
                                reservation.getSlotId())
                        .orElseThrow(() ->
                                new ParkingSlotUnavailableException(
                                        "Parking slot not found."));

        ReservationResponse response =
                reservationMapper.toResponse(reservation);

        response.setFloorLevel(slot.getFloorLevel());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

}
