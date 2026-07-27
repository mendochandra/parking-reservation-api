package com.gtech.gtech.service;

import com.gtech.gtech.api.mappers.ReservationMapper;
import com.gtech.gtech.api.request.ReservationRequest;
import com.gtech.gtech.domain.entity.ParkingLot;
import com.gtech.gtech.domain.entity.ParkingSlot;
import com.gtech.gtech.domain.entity.Reservation;
import com.gtech.gtech.domain.enums.ParkingSlotStatus;
import com.gtech.gtech.domain.enums.ReservationStatus;
import com.gtech.gtech.domain.enums.VehicleType;
import com.gtech.gtech.exception.ReservationConflictException;
import com.gtech.gtech.repository.ParkingLotRepository;
import com.gtech.gtech.repository.ParkingSlotRepository;
import com.gtech.gtech.repository.ReservationRepository;
import com.gtech.gtech.service.parking.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Test
    void shouldCreateReservationAndReserveSlot() {

        ReservationRequest request = new ReservationRequest();
        request.setLotId("LOT-001");
        request.setVehicleType(VehicleType.CAR);
        request.setCustomerId("CUS-001");
        request.setLicensePlate("B1234AA");
        request.setPlannedStartTime(
                LocalDateTime.of(2026, 3, 15, 10, 0));
        request.setPlannedEndTime(
                LocalDateTime.of(2026, 3, 15, 12, 0));

        ParkingLot lot = new ParkingLot();
        lot.setLotId("LOT-001");

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId("A-01");
        slot.setVehicleType(VehicleType.CAR);
        slot.setStatus(ParkingSlotStatus.AVAILABLE);
        slot.setParkingLot(lot);

        Reservation reservation = new Reservation();

        reservation.setLotId("LOT-001");
        reservation.setVehicleType(VehicleType.CAR);
        reservation.setCustomerId("CUS-001");
        reservation.setLicensePlate("B1234AA");
        reservation.setPlannedStartTime(request.getPlannedStartTime());
        reservation.setPlannedEndTime(request.getPlannedEndTime());

        // Parking lot exists
        when(parkingLotRepository.existsById("LOT-001"))
                .thenReturn(true);

        // findAvailableSlot() -> getAvailableSlots()
        when(parkingSlotRepository
                .findByParkingLotLotIdAndVehicleTypeOrderByFloorLevelAscSlotIdAsc(
                        "LOT-001",
                        VehicleType.CAR))
                .thenReturn(List.of(slot));

        // isSlotAvailable()
        when(reservationRepository.findByLotIdAndSlotIdAndStatusIn(
                eq("LOT-001"),
                eq("A-01"),
                any()))
                .thenReturn(List.of());

        // lock slot
        when(parkingSlotRepository.lockSlot(
                "LOT-001",
                "A-01"))
                .thenReturn(Optional.of(slot));

        // mapper
        when(reservationMapper.toEntity(any(ReservationRequest.class)))
                .thenReturn(reservation);

        // save slot
        when(parkingSlotRepository.save(any(ParkingSlot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // save reservation
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result =
                reservationService.createReservation(request);

        assertNotNull(result);

        assertEquals(
                ReservationStatus.PENDING,
                result.getStatus());

        assertEquals(
                "A-01",
                result.getSlotId());

        assertEquals(
                ParkingSlotStatus.RESERVED,
                slot.getStatus());

        verify(parkingSlotRepository).save(slot);
        verify(reservationRepository).save(reservation);
    }
    @Test
    void shouldThrowExceptionWhenDoubleBooking() {

        ReservationRequest request = new ReservationRequest();

        request.setLotId("LOT-001");
        request.setVehicleType(VehicleType.CAR);
        request.setCustomerId("CUS-001");
        request.setLicensePlate("B1234AA");
        request.setPlannedStartTime(
                LocalDateTime.of(2026, 3, 15, 10, 30));
        request.setPlannedEndTime(
                LocalDateTime.of(2026, 3, 15, 11, 30));

        ParkingLot lot = new ParkingLot();
        lot.setLotId("LOT-001");

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId("A-01");
        slot.setParkingLot(lot);
        slot.setVehicleType(VehicleType.CAR);
        slot.setStatus(ParkingSlotStatus.AVAILABLE);

        Reservation existingReservation = new Reservation();
        existingReservation.setReservationId("RES-001");
        existingReservation.setLotId("LOT-001");
        existingReservation.setSlotId("A-01");
        existingReservation.setStatus(ReservationStatus.PENDING);

        existingReservation.setPlannedStartTime(
                LocalDateTime.of(2026, 3, 15, 10, 0));

        existingReservation.setPlannedEndTime(
                LocalDateTime.of(2026, 3, 15, 12, 0));

        when(parkingLotRepository.existsById("LOT-001"))
                .thenReturn(true);

        when(parkingSlotRepository
                .findByParkingLotLotIdAndVehicleTypeOrderByFloorLevelAscSlotIdAsc(
                        "LOT-001",
                        VehicleType.CAR))
                .thenReturn(List.of(slot));

        when(parkingSlotRepository.lockSlot(
                "LOT-001",
                "A-01"))
                .thenReturn(Optional.of(slot));

        when(reservationRepository.findByLotIdAndSlotIdAndStatusIn(
                eq("LOT-001"),
                eq("A-01"),
                any()))
                .thenReturn(List.of(existingReservation));

        assertThrows(
                ReservationConflictException.class,
                () -> reservationService.createReservation(request));
    }

    @Test
    void shouldBecomeNoShowWhenLateCheckIn() {

        Reservation reservation = new Reservation();

        reservation.setLotId("LOT-001");
        reservation.setSlotId("A-01");
        reservation.setStatus(ReservationStatus.PENDING);

        reservation.setPlannedStartTime(
                LocalDateTime.of(2026,3,15,10,0));

        ParkingSlot slot = new ParkingSlot();

        slot.setStatus(ParkingSlotStatus.RESERVED);

        when(reservationRepository.findById(any()))
                .thenReturn(Optional.of(reservation));

        when(parkingSlotRepository
                .findByParkingLotLotIdAndSlotId(any(), any()))
                .thenReturn(Optional.of(slot));

        when(parkingSlotRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(reservationRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Reservation result =
                reservationService.checkIn(
                        "1",
                        LocalDateTime.of(2026,3,15,10,35));

        assertEquals(
                ReservationStatus.NO_SHOW,
                result.getStatus());

        assertEquals(
                ParkingSlotStatus.AVAILABLE,
                slot.getStatus());
    }

    @Test
    void shouldReleaseSlotWhenCancelReservation() {

        Reservation reservation = new Reservation();

        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setLotId("LOT-001");
        reservation.setSlotId("A-01");

        reservation.setPlannedStartTime(
                LocalDateTime.now().plusHours(1));

        ParkingSlot slot = new ParkingSlot();
        slot.setStatus(ParkingSlotStatus.RESERVED);

        when(reservationRepository.findById(any()))
                .thenReturn(Optional.of(reservation));

        when(parkingSlotRepository
                .findByParkingLotLotIdAndSlotId(any(), any()))
                .thenReturn(Optional.of(slot));

        when(parkingSlotRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(reservationRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        Reservation result =
                reservationService.cancelReservation(
                        "1",
                        "Cancel");

        assertEquals(
                ReservationStatus.CANCELLED,
                result.getStatus());

        assertEquals(
                ParkingSlotStatus.AVAILABLE,
                slot.getStatus());

        assertFalse(result.isLateCancellation());
    }

    @Test
    void shouldThrowExceptionWhenExtendToBookedWindow() {

        Reservation active = new Reservation();

        active.setReservationId("RES-001");
        active.setStatus(ReservationStatus.ACTIVE);
        active.setLotId("LOT-001");
        active.setSlotId("A-01");

        active.setPlannedStartTime(
                LocalDateTime.of(2026, 3, 15, 10, 0));

        active.setPlannedEndTime(
                LocalDateTime.of(2026, 3, 15, 12, 0));

        Reservation conflict = new Reservation();

        conflict.setReservationId("RES-002");

        conflict.setStatus(ReservationStatus.ACTIVE);

        conflict.setPlannedStartTime(
                LocalDateTime.of(2026, 3, 15, 12, 30));

        conflict.setPlannedEndTime(
                LocalDateTime.of(2026, 3, 15, 15, 0));

        when(reservationRepository.findById(any()))
                .thenReturn(Optional.of(active));

        when(reservationRepository.findBySlotIdAndStatusIn(
                any(),
                any()))
                .thenReturn(List.of(conflict));

        assertThrows(
                ReservationConflictException.class,
                () -> reservationService.extendReservation(
                        "RES-001",
                        LocalDateTime.of(2026, 3, 15, 14, 0)));
    }
}