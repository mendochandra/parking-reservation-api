package com.gtech.gtech.service.parking;

import com.gtech.gtech.api.request.ReservationRequest;
import com.gtech.gtech.api.response.ReservationResponse;
import com.gtech.gtech.domain.entity.ParkingSlot;
import com.gtech.gtech.domain.entity.Reservation;
import com.gtech.gtech.domain.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    List<ParkingSlot> getAvailableSlots(
            String lotId,
            VehicleType vehicleType,
            LocalDateTime startTime,
            LocalDateTime endTime);

    // Find the best available slot for a vehicle type and time window
    Optional<ParkingSlot> findAvailableSlot(
            String lotId,
            VehicleType vehicleType,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    // Create and confirm a reservation atomically
    Reservation createReservation(ReservationRequest request);

    // Mark a reservation as active (vehicle checked in)
    Reservation checkIn(String reservationId, LocalDateTime actualStartTime);

    // Complete a reservation (vehicle checked out), triggers billing
    Reservation checkOut(String reservationId, LocalDateTime actualEndTime);

    // Cancel a reservation; apply cancellation rules
    Reservation cancelReservation(String reservationId, String reason);

    // Extend an active reservation if the slot is still available
    Reservation extendReservation(String reservationId, LocalDateTime newEndTime);

    List<ReservationResponse> getAllReservations();


}
