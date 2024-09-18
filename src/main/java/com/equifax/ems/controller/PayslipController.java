package com.equifax.ems.controller;

import com.equifax.ems.entity.Payslip;
import com.equifax.ems.service.PayslipService;
import com.equifax.ems.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/payslip")
public class PayslipController {

    @Autowired
    private PayslipService payslipService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllPayslips() {
        List<Payslip> payslips = payslipService.fetchAllPayslips();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), payslips, null));
    }

    @PostMapping("/generate/{id}")
    public ResponseEntity<ApiResponse> generatePayslip(@PathVariable Long id,
                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        Payslip payslip = payslipService.generatepayslip(id, startDate, endDate);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), payslip, null));
    }
}
