package com.equifax.ems.controller;

import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Payslip;
import com.equifax.ems.service.PayslipService;
import com.equifax.ems.utility.ApiResponse;
import com.equifax.ems.utility.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PayslipControllerTest {

    @InjectMocks
    private PayslipController payslipController;

    @Mock
    private PayslipService payslipService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPayslips_Success() {
        List<Payslip> payslipList = new ArrayList<>();
        Payslip payslip = new Payslip();
        payslip.setPayslipId(1L);
        payslip.setGenerateDate(new Date());
        payslip.setStartDate(new Date());
        payslip.setEndDate(new Date());
        payslip.setTax(100.0);
        payslip.setNetPay(900.0);
        payslipList.add(payslip);

        when(payslipService.fetchAllPayslips()).thenReturn(payslipList);

        ResponseEntity<ApiResponse> response = payslipController.getAllPayslips();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(payslipList, response.getBody().getData());
        verify(payslipService, times(1)).fetchAllPayslips();
    }

    @Test
    public void testGeneratePayslip_Success() {
        Long empId = 1L;
        Date startDate = new Date();
        Date endDate = new Date();
        Payslip generatedPayslip = new Payslip();
        generatedPayslip.setPayslipId(1L);
        generatedPayslip.setEmployee(new Employee());
        generatedPayslip.setGenerateDate(new Date());
        generatedPayslip.setStartDate(startDate);
        generatedPayslip.setEndDate(endDate);
        generatedPayslip.setTax(100.0);
        generatedPayslip.setNetPay(900.0);

        when(payslipService.generatepayslip(empId, startDate, endDate)).thenReturn(generatedPayslip);

        ResponseEntity<ApiResponse> response = payslipController.generatePayslip(empId, startDate, endDate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(generatedPayslip, response.getBody().getData());
        verify(payslipService, times(1)).generatepayslip(empId, startDate, endDate);
    }

    @Test
    public void testGeneratePayslip_EmployeeNotFound() {
        Long empId = 1L;
        Date startDate = new Date();
        Date endDate = new Date();

        when(payslipService.generatepayslip(empId, startDate, endDate))
                .thenThrow(new CustomException("Employee not found"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            payslipController.generatePayslip(empId, startDate, endDate);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(payslipService, times(1)).generatepayslip(empId, startDate, endDate);
    }
    @Test
    public void testGeneratePayslipAll_Success() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date();
        List<Payslip> payslips = new ArrayList<>();
        payslips.add(new Payslip()); // Add a sample Payslip

        when(payslipService.generatePayslipAll(startDate, endDate)).thenReturn(payslips);

        // Act
        ResponseEntity<ApiResponse> response = payslipController.generatePayslipALl(startDate, endDate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(payslips, response.getBody().getData());
    }

    @Test
    public void testGeneratePayslipAll_Failure() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date();

        // Simulate a failure scenario, e.g., service throws an exception
        when(payslipService.generatePayslipAll(any(Date.class), any(Date.class)))
                .thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<ApiResponse> response;
        try {
            response = payslipController.generatePayslipALl(startDate, endDate);
        } catch (RuntimeException e) {
            // Assert
            assertEquals("Service error", e.getMessage());
            return; // Exit the test
        }

        // If no exception was thrown, fail the test
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
