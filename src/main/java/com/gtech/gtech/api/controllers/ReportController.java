package com.gtech.gtech.api.controllers;

import com.gtech.gtech.api.response.ReportResponse;
import com.gtech.gtech.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<ReportResponse> summary() {

        return ResponseEntity.ok(reportService.getSummary());

    }

}