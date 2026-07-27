package com.gtech.gtech.api.controllers;


import com.gtech.gtech.api.response.ParkingInvoiceResponse;
import com.gtech.gtech.service.billing.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/{reservationId}")
    public ResponseEntity<ParkingInvoiceResponse> getInvoice(@PathVariable String reservationId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(billingService.getInvoice(reservationId));

    }

}
