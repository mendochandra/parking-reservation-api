package com.gtech.gtech.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private long totalReservations;

    private long completedReservations;

    private long cancelledReservations;

    private BigDecimal totalRevenue;

}
