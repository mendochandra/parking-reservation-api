package com.gtech.gtech.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParkingInvoice {

    @Id
    private String invoiceId;

    @NotBlank
    private String reservationId;

    @NotBlank
    private String customerId;

    //after grace period deduction
    private long billedDurationMinutes;

    private BigDecimal baseAmount;

    private BigDecimal overnightSurchargeApplied;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private LocalDateTime generatedAt;

}
