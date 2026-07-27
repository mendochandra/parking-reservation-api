package com.gtech.gtech.repository;

import com.gtech.gtech.domain.entity.ParkingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingInvoiceRepository extends JpaRepository<ParkingInvoice, String> {

    Optional<ParkingInvoice> findByReservationId(String reservationId);

}