package com.gtech.gtech.service.billing;

import com.gtech.gtech.api.mappers.ParkingInvoiceMapper;
import com.gtech.gtech.api.response.ParkingInvoiceResponse;
import com.gtech.gtech.domain.entity.ParkingInvoice;
import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.domain.entity.Reservation;
import com.gtech.gtech.exception.ReservationNotFoundException;
import com.gtech.gtech.repository.ParkingInvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BillingServiceImpl implements BillingService {

    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private final ParkingInvoiceRepository parkingInvoiceRepository;
    private final ParkingInvoiceMapper parkingInvoiceMapper;

    @Override
    public ParkingInvoice generateInvoice(Reservation reservation, RateCard rateCard) {

        BigDecimal baseAmount = calculateBaseAmount(
                reservation.getActualStartTime(),
                reservation.getActualEndTime(),
                rateCard);

        int midnightCrossings = countMidnightCrossings(
                reservation.getActualStartTime(),
                reservation.getActualEndTime());

        BigDecimal totalAmount = calculateAmount(
                reservation.getActualStartTime(),
                reservation.getActualEndTime(),
                rateCard);

        BigDecimal overnightCharge = rateCard.getOvernightSurcharge().multiply(BigDecimal.valueOf(midnightCrossings));

        BigDecimal discount = BigDecimal.ZERO;

        ParkingInvoice invoice = new ParkingInvoice();
        invoice.setInvoiceId(UUID.randomUUID().toString());
        invoice.setReservationId(reservation.getReservationId());
        invoice.setCustomerId(reservation.getCustomerId());
        invoice.setBilledDurationMinutes(calculateBillableMinutes(reservation.getActualStartTime(), reservation.getActualEndTime(), rateCard.getGracePeriodMinutes()));
        invoice.setBaseAmount(baseAmount.setScale(2, RoundingMode.HALF_UP));
        invoice.setOvernightSurchargeApplied(overnightCharge.setScale(2, RoundingMode.HALF_UP));
        invoice.setDiscountAmount(discount.setScale(2, RoundingMode.HALF_UP));
        invoice.setTotalAmount(totalAmount);
        invoice.setGeneratedAt(LocalDateTime.now());

        return parkingInvoiceRepository.save(invoice);
    }

    @Override
    public BigDecimal calculateAmount(LocalDateTime actualStart, LocalDateTime actualEnd, RateCard rateCard) {

        BigDecimal baseAmount = calculateBaseAmount(actualStart, actualEnd, rateCard);

        int midnightCrossings = countMidnightCrossings(actualStart, actualEnd);

        BigDecimal overnightCharge = rateCard.getOvernightSurcharge().multiply(BigDecimal.valueOf(midnightCrossings));
        BigDecimal discount = BigDecimal.ZERO;

        return baseAmount.add(overnightCharge).subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public ParkingInvoiceResponse getInvoice(String reservationId) {
        ParkingInvoice invoice = parkingInvoiceRepository
                .findByReservationId(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException(
                                "Invoice not found"));

        return parkingInvoiceMapper.toResponse(invoice);
    }

    private BigDecimal calculateBaseAmount(LocalDateTime start, LocalDateTime end, RateCard rateCard) {

        BigDecimal total = BigDecimal.ZERO;
        LocalDate currentDate = start.toLocalDate();
        LocalDate lastDate = end.toLocalDate();

        boolean graceApplied = false;

        while (!currentDate.isAfter(lastDate)) {
            LocalDateTime dayStart =
                    currentDate.equals(start.toLocalDate())
                            ? start
                            : currentDate.atStartOfDay();

            LocalDateTime dayEnd =
                    currentDate.equals(end.toLocalDate())
                            ? end
                            : currentDate.atTime(LocalTime.MAX);

            long minutes =
                    Duration.between(dayStart, dayEnd).toMinutes();

            if (!graceApplied) {
                minutes -= rateCard.getGracePeriodMinutes();
                graceApplied = true;
            }

            if (minutes > 0) {
                long blocks = (minutes + 29) / 30;
                BigDecimal charge =
                        rateCard.getHourlyRate()
                                .divide(TWO, 2, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(blocks));

                if (charge.compareTo(rateCard.getDailyCap()) > 0) {
                    charge = rateCard.getDailyCap();
                }

                total = total.add(charge);
            }

            currentDate = currentDate.plusDays(1);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private long calculateBillableMinutes(LocalDateTime start, LocalDateTime end, int graceMinutes) {

        long minutes = Duration.between(start, end).toMinutes();
        minutes -= graceMinutes;

        return Math.max(minutes, 0);
    }

    private int countMidnightCrossings(LocalDateTime start, LocalDateTime end) {

        return (int) Duration.between(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay()).toDays();
    }
}