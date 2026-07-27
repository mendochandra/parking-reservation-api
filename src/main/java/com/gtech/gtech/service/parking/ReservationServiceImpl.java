package com.gtech.gtech.service.parking;

import com.gtech.gtech.api.mappers.ReservationMapper;
import com.gtech.gtech.api.request.ReservationRequest;
import com.gtech.gtech.api.response.ReservationResponse;
import com.gtech.gtech.domain.entity.ParkingSlot;
import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.domain.entity.Reservation;
import com.gtech.gtech.domain.enums.ParkingSlotStatus;
import com.gtech.gtech.domain.enums.ReservationStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import com.gtech.gtech.exception.ParkingSlotUnavailableException;
import com.gtech.gtech.exception.ReservationConflictException;
import com.gtech.gtech.exception.ReservationNotFoundException;
import com.gtech.gtech.repository.*;
import com.gtech.gtech.service.billing.BillingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ReservationRepository reservationRepository;
    private final ParkingInvoiceRepository parkingInvoiceRepository;
    private final RateCardRepository rateCardRepository;
    private final BillingService billingService;
    private final ReservationMapper reservationMapper;

    @Override
    public List<ParkingSlot> getAvailableSlots(String lotId, VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime) {

        if (!parkingLotRepository.existsById(lotId)) {
            throw new ParkingSlotUnavailableException(
                    "Parking lot not found.");
        }

        return parkingSlotRepository
                .findByParkingLotLotIdAndVehicleTypeOrderByFloorLevelAscSlotIdAsc(
                        lotId,
                        vehicleType)
                .stream()
                .filter(slot -> slot.getStatus() == ParkingSlotStatus.AVAILABLE)
                .toList();
    }

    @Override
    public Optional<ParkingSlot> findAvailableSlot(String lotId, VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime) {

        return getAvailableSlots(
                lotId,
                vehicleType,
                startTime,
                endTime)
                .stream()
                .findFirst();
    }

    @Override
    public Reservation createReservation(ReservationRequest request) {
        // 1. Pastikan parking lot ada
        if (!parkingLotRepository.existsById(request.getLotId())) {
            throw new ParkingSlotUnavailableException(
                    "Parking lot not found : " + request.getLotId());
        }

        // 2. Cari slot terbaik yang tersedia
        ParkingSlot slot = findAvailableSlot(
                request.getLotId(),
                request.getVehicleType(),
                request.getPlannedStartTime(),
                request.getPlannedEndTime())
                .orElseThrow(() ->
                        new ParkingSlotUnavailableException(
                                "No available parking slot."));

        // 3. Lock slot agar tidak terjadi double booking
        ParkingSlot lockedSlot = parkingSlotRepository
                .lockSlot(
                        request.getLotId(),
                        slot.getSlotId())
                .orElseThrow(() ->
                        new ParkingSlotUnavailableException(
                                "Parking slot not found."));

        // 4. Double check conflict setelah mendapatkan lock
        List<Reservation> conflicts =
                reservationRepository.findByLotIdAndSlotIdAndStatusIn(
                        request.getLotId(),
                        lockedSlot.getSlotId(),
                        List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.ACTIVE));

        boolean overlap = conflicts.stream()
                .anyMatch(r ->
                        request.getPlannedStartTime().isBefore(r.getPlannedEndTime())
                                && request.getPlannedEndTime().isAfter(r.getPlannedStartTime()));

        if (overlap) {
            throw new ReservationConflictException(
                    "Parking slot is already reserved.");
        }

        // 5. Mapping request -> entity
        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setSlotId(lockedSlot.getSlotId());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setLateCancellation(false);

        // 6. Update status slot
        lockedSlot.setStatus(ParkingSlotStatus.RESERVED);

        parkingSlotRepository.save(lockedSlot);
        return reservationRepository.save(reservation);

    }

    @Override
    public Reservation checkIn(String reservationId, LocalDateTime actualStartTime) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new ReservationConflictException("Reservation cannot be checked in.");
        }

        ParkingSlot slot = parkingSlotRepository.findByParkingLotLotIdAndSlotId(
                        reservation.getLotId(),
                        reservation.getSlotId())
                .orElseThrow(() ->
                        new ParkingSlotUnavailableException(
                                "Parking slot not found"));

        LocalDateTime latestCheckIn = reservation.getPlannedStartTime().plusMinutes(30);

        if (actualStartTime.isAfter(latestCheckIn)) {

            reservation.setStatus(ReservationStatus.NO_SHOW);

            slot.setStatus(ParkingSlotStatus.AVAILABLE);

            parkingSlotRepository.save(slot);

            return reservationRepository.save(reservation);
        }

        reservation.setActualStartTime(actualStartTime);
        reservation.setStatus(ReservationStatus.ACTIVE);

        slot.setStatus(ParkingSlotStatus.OCCUPIED);

        parkingSlotRepository.save(slot);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation checkOut(String reservationId, LocalDateTime actualEndTime) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new ReservationConflictException("Reservation is not active.");
        }

        reservation.setActualEndTime(actualEndTime);
        reservation.setStatus(ReservationStatus.COMPLETED);

        ParkingSlot slot = parkingSlotRepository.findByParkingLotLotIdAndSlotId(
                                reservation.getLotId(),
                                reservation.getSlotId())
                        .orElseThrow(() ->
                                new ParkingSlotUnavailableException(
                                        "Parking slot not found"));

        slot.setStatus(ParkingSlotStatus.AVAILABLE);

        parkingSlotRepository.save(slot);

        Reservation savedReservation = reservationRepository.save(reservation);

        RateCard rateCard = rateCardRepository.findByVehicleType(savedReservation.getVehicleType()).orElseThrow(() -> new ReservationNotFoundException("Rate card not found."));

        parkingInvoiceRepository.save(billingService.generateInvoice(savedReservation, rateCard));

        return savedReservation;
    }

    @Override
    public Reservation cancelReservation(String reservationId, String reason) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING && reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new ReservationConflictException("Reservation cannot be cancelled.");
        }

        LocalDateTime lateCancellationTime = reservation.getPlannedStartTime().minusMinutes(30);

        reservation.setLateCancellation(LocalDateTime.now().isAfter(lateCancellationTime));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setReason(reason);

        ParkingSlot slot = parkingSlotRepository.findByParkingLotLotIdAndSlotId(
                                reservation.getLotId(),
                                reservation.getSlotId())
                        .orElseThrow(() ->
                                new ParkingSlotUnavailableException(
                                        "Parking slot not found"));

        slot.setStatus(ParkingSlotStatus.AVAILABLE);

        parkingSlotRepository.save(slot);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation extendReservation(String reservationId, LocalDateTime newEndTime) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new ReservationConflictException(
                    "Only ACTIVE reservation can be extended.");
        }

        if (!newEndTime.isAfter(reservation.getPlannedEndTime())) {
            throw new IllegalArgumentException(
                    "New end time must be after current planned end time.");
        }

        // Ambil reservation lain yang masih PENDING atau ACTIVE pada slot yang sama
        List<Reservation> reservations =
                reservationRepository.findBySlotIdAndStatusIn(
                        reservation.getSlotId(),
                        List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.ACTIVE
                        ));

        // Cek apakah extension bertabrakan dengan reservation lain
        boolean overlap = reservations.stream()
                .filter(r -> !r.getReservationId().equals(reservationId))
                .anyMatch(r ->
                        reservation.getPlannedStartTime().isBefore(r.getPlannedEndTime())
                                && newEndTime.isAfter(r.getPlannedStartTime()));

        if (overlap) {
            throw new ReservationConflictException(
                    "Extension conflicts with another reservation.");
        }

        reservation.setPlannedEndTime(newEndTime);

        return reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservation -> {
                    ParkingSlot slot = parkingSlotRepository
                            .findByParkingLotLotIdAndSlotId(
                                    reservation.getLotId(),
                                    reservation.getSlotId())
                            .orElseThrow(() -> new ParkingSlotUnavailableException("Parking slot not found."));

                    ReservationResponse response = reservationMapper.toResponse(reservation);
                    response.setFloorLevel(slot.getFloorLevel());
                    return response;
                }).toList();
    }


}
