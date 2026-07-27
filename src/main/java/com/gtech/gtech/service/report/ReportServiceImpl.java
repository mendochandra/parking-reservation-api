package com.gtech.gtech.service.report;

import com.gtech.gtech.api.response.ReportResponse;
import com.gtech.gtech.domain.entity.ParkingInvoice;
import com.gtech.gtech.domain.entity.Reservation;
import com.gtech.gtech.domain.enums.ReservationStatus;
import com.gtech.gtech.repository.ParkingInvoiceRepository;
import com.gtech.gtech.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReservationRepository reservationRepository;

    private final ParkingInvoiceRepository parkingInvoiceRepository;

    @Override
    public ReportResponse getSummary() {

        List<Reservation> reservations = reservationRepository.findAll();

        List<ParkingInvoice> invoices = parkingInvoiceRepository.findAll();

        long completed =
                reservations.stream()
                        .filter(r ->
                                r.getStatus() ==
                                        ReservationStatus.COMPLETED)
                        .count();

        long cancelled =
                reservations.stream()
                        .filter(r ->
                                r.getStatus() ==
                                        ReservationStatus.CANCELLED)
                        .count();

        BigDecimal revenue =
                invoices.stream()
                        .map(ParkingInvoice::getTotalAmount)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        return new ReportResponse(reservations.size(), completed, cancelled, revenue);
    }

}
