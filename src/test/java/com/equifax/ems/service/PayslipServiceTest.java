package com.equifax.ems.service;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Payslip;
import com.equifax.ems.repository.PayslipRepository;
import com.equifax.ems.utility.CustomException;
import com.equifax.ems.utility.UtilityMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PayslipServiceTest {

    @InjectMocks
    private PayslipService payslipService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private PayslipRepository payslipRepository;

    @Mock
    private BonusService bonusService;

    @Mock
    private DeductionService deductionService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setSalary(600000);
        employee.setHireDate(Date.from(LocalDate.of(2020, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    @Test
    public void testFetchAllPayslips() {
        Payslip payslip1 = new Payslip();
        Payslip payslip2 = new Payslip();
        List<Payslip> expectedPayslips = Arrays.asList(payslip1, payslip2);

        when(payslipRepository.findAll()).thenReturn(expectedPayslips);

        List<Payslip> actualPayslips = payslipService.fetchAllPayslips();

        assertNotNull(actualPayslips);
        assertEquals(2, actualPayslips.size());
        assertEquals(expectedPayslips, actualPayslips);

        verify(payslipRepository).findAll();
    }

    @Test
    void testGeneratePayslip_Success() {
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        when(payslipRepository.findPayslipByEmployeeAndDateRange(1L, startDate, endDate)).thenReturn(Optional.empty());
        when(bonusService.getBonusesForEmployee(1L, startDate, endDate)).thenReturn(List.of(new Bonus(1L, 1000.0, new Date(), employee)));
        when(deductionService.getDeductionForEmployee(1L, startDate, endDate)).thenReturn(List.of(new Deduction(1L, 500.0, new Date(), employee)));
        when(payslipRepository.save(any(Payslip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        double basicCTC = employee.getSalary();
        double perDaySalary = basicCTC / 365;
        long daysInThePeriod = 30;
        double paymentForPeriod = daysInThePeriod * perDaySalary;
        double totalBonuses = 1000.0;
        double totalDeductions = 500.0;
        double netAmount = paymentForPeriod + totalBonuses - totalDeductions;

        double taxPercentage = UtilityMethods.calculateTaxSlab(basicCTC);
        double taxAmount = netAmount * taxPercentage / 100;
        double netPay = netAmount - taxAmount;

        Payslip payslip = payslipService.generatepayslip(1L, startDate, endDate);

        assertNotNull(payslip);
        assertEquals(1L, payslip.getEmployee().getEmployeeId());
        assertEquals(taxAmount, payslip.getTax(), 0.01);
        assertEquals(netPay, payslip.getNetPay(), 0.01);
    }

    @Test
    void testGeneratePayslip_EmployeeNotFound() {
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(employeeService.getEmployeeById(1L)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> {
            payslipService.generatepayslip(1L, startDate, endDate);
        });

        assertEquals("Employee not found with id: 1", exception.getMessage());
    }

    @Test
    void testGeneratePayslip_ExistingPayslip() {
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Payslip existingPayslip = new Payslip();
        existingPayslip.setEmployee(employee);
        existingPayslip.setStartDate(startDate);
        existingPayslip.setEndDate(endDate);

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        when(payslipRepository.findPayslipByEmployeeAndDateRange(1L, startDate, endDate)).thenReturn(Optional.of(existingPayslip));

        Payslip payslip = payslipService.generatepayslip(1L, startDate, endDate);

        assertNotNull(payslip);
        assertEquals(existingPayslip, payslip);
    }

    @Test
    void testGeneratePayslip_StartDateAfterEndDate() {

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        Date startDate = Date.from(LocalDate.of(2023, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        CustomException exception = assertThrows(CustomException.class, () -> {
            payslipService.generatepayslip(1L, startDate, endDate);
        });

        assertEquals("Oops! The start date is after the end date.", exception.getMessage());
    }

    @Test
    void testGeneratePayslip_ErrorFetchingBonuses() {
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        when(payslipRepository.findPayslipByEmployeeAndDateRange(1L, startDate, endDate)).thenReturn(Optional.empty());
        when(bonusService.getBonusesForEmployee(1L, startDate, endDate)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            payslipService.generatepayslip(1L, startDate, endDate);
        });

        assertEquals("Error fetching bonuses for employee: Database error", exception.getMessage());
    }

    @Test
    void testGeneratePayslip_ErrorFetchingDeductions() {
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);
        when(payslipRepository.findPayslipByEmployeeAndDateRange(1L, startDate, endDate)).thenReturn(Optional.empty());
        when(deductionService.getDeductionForEmployee(1L, startDate, endDate)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> {
            payslipService.generatepayslip(1L, startDate, endDate);
        });

        assertEquals("Error fetching deductions for employee: Database error", exception.getMessage());
    }
}
