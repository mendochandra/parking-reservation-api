package com.gtech.gtech.repository;

import com.gtech.gtech.domain.entity.Reservation;
import com.gtech.gtech.domain.enums.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findBySlotIdAndStatusIn(
            String slotId,
            List<ReservationStatus> statuses
    );

    List<Reservation> findByLotIdAndSlotIdAndStatusIn(String lotId, String slotId, Collection<ReservationStatus> statuses);
}
