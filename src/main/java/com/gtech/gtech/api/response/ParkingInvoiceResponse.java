package com.gtech.gtech.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInvoiceResponse {

    private String invoiceId;

    private String reservationId;

    private String customerId;

    private Long billedDurationMinutes;

    private String baseAmount;

    private String overnightSurchargeApplied;

    private String discountAmount;

    private String totalAmount;

    private LocalDateTime generatedAt;
}
