package com.gtech.gtech.service.billing;

import com.gtech.gtech.api.response.ParkingInvoiceResponse;
import com.gtech.gtech.domain.entity.ParkingInvoice;
import com.gtech.gtech.domain.entity.RateCard;
import com.gtech.gtech.domain.entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface BillingService {

    ParkingInvoice generateInvoice(
            Reservation reservation,
            RateCard rateCard
    );

    BigDecimal calculateAmount(
            LocalDateTime actualStart,
            LocalDateTime actualEnd,
            RateCard rateCard
    );

    ParkingInvoiceResponse getInvoice(String reservationId);

}
