package com.equifax.ems.controller;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Pay;
import com.equifax.ems.service.BonusService;
import com.equifax.ems.service.DeductionService;
import com.equifax.ems.service.EmployeeService;
import com.equifax.ems.service.PayService;
import com.equifax.ems.utility.ApiResponse;
import com.equifax.ems.utility.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private BonusService bonusService;

    @Mock
    private DeductionService deductionService;

    @Mock
    private PayService payService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees_Success() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeService.fetchAllEmployees()).thenReturn(employees);

        ResponseEntity<ApiResponse> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employees, response.getBody().getData());
    }

    @Test
    public void testGetAllEmployees_NoEmployees() {
        when(employeeService.fetchAllEmployees()).thenReturn(List.of());
        ResponseEntity<ApiResponse> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(List.class, response.getBody().getData());
        assertTrue(((List<?>) response.getBody().getData()).isEmpty());
    }


    @Test
    public void testGetEmployeeById_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employee);

        ResponseEntity<ApiResponse> response = employeeController.getEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody().getData());
    }

    @Test
    public void testGetEmployeeById_Failure() {
        Long employeeId = 1L;
        when(employeeService.getEmployeeById(employeeId)).thenReturn(null);

        ResponseEntity<ApiResponse> response = employeeController.getEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getData());
    }

    @Test
    public void testSaveEmployee_Success() {
        Employee employee = new Employee();
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        ResponseEntity<ApiResponse> response = employeeController.saveEmployee(employee);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employee, response.getBody().getData());
    }

    @Test
    public void testUpdateEmployee_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        when(employeeService.updateEmployeeById(eq(employeeId), any(Employee.class))).thenReturn(employee);

        ResponseEntity<ApiResponse> response = employeeController.updateEmployee(employeeId, employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody().getData());
    }

    @Test
    public void testDeleteEmployee_Success() {
        Long employeeId = 1L;
        String message = "Employee deleted successfully";
        when(employeeService.deleteEmployeeById(employeeId)).thenReturn(message);

        ResponseEntity<ApiResponse> response = employeeController.deleteEmployee(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody().getData());
    }

    @Test
    public void testAddEmployeeBonus_Success() {
        Long employeeId = 1L;
        Bonus bonus = new Bonus();
        when(employeeService.addEmployeeBonus(eq(employeeId), anyString(), anyDouble(), any(Date.class))).thenReturn(bonus);

        ResponseEntity<ApiResponse> response = employeeController.addEmployeeBonus(employeeId, "Bonus Name", 100.0, new Date());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bonus, response.getBody().getData());
    }

    @Test
    public void testAddEmployeeDeduction_Success() {
        Long employeeId = 1L;
        Deduction deduction = new Deduction();
        when(employeeService.addEmployeeDeduction(eq(employeeId), anyString(), anyDouble(), any(Date.class))).thenReturn(deduction);

        ResponseEntity<ApiResponse> response = employeeController.addEmployeeDeduction(employeeId, "Deduction Name", 50.0, new Date());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(deduction, response.getBody().getData());
    }

    @Test
    void testGetBonusesByIdAndDate_Success() throws Exception {
        Long employeeId = 1L;
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Bonus> bonuses = List.of(new Bonus());

        when(bonusService.getBonusesForEmployee(employeeId, startDate, endDate)).thenReturn(bonuses);

        ResponseEntity<ApiResponse> response = employeeController.getBonusesByIdAndDate(employeeId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bonuses, response.getBody().getData());
        verify(bonusService, times(1)).getBonusesForEmployee(employeeId, startDate, endDate);
    }

    @Test
    void testGetBonusesByIdAndDate_EmployeeNotFound() throws Exception {
        Long employeeId = 1L;
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(bonusService.getBonusesForEmployee(employeeId, startDate, endDate)).thenThrow(new CustomException("Employee not found"));

        Exception exception = assertThrows(CustomException.class, () -> {
            employeeController.getBonusesByIdAndDate(employeeId, startDate, endDate);
        });

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void testGetDeductionsByIdAndDate_Success() throws Exception {
        Long employeeId = 1L;
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Deduction> deductions = List.of(new Deduction());

        when(deductionService.getDeductionForEmployee(employeeId, startDate, endDate)).thenReturn(deductions);

        ResponseEntity<ApiResponse> response = employeeController.getDeductionsByIdAndDate(employeeId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(deductions, response.getBody().getData());
        verify(deductionService, times(1)).getDeductionForEmployee(employeeId, startDate, endDate);
    }

    @Test
    void testGetDeductionsByIdAndDate_EmployeeNotFound() throws Exception {
        Long employeeId = 1L;
        Date startDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(LocalDate.of(2023, 1, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

        when(deductionService.getDeductionForEmployee(employeeId, startDate, endDate)).thenThrow(new CustomException("Employee not found"));

        Exception exception = assertThrows(CustomException.class, () -> {
            employeeController.getDeductionsByIdAndDate(employeeId, startDate, endDate);
        });

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    public void testGetPay_Success() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumber("1234567890");
        employee.setHireDate(new Date());
        employee.setSalary(50000.0);

        Pay pay = new Pay();
        pay.setPayId(1L);
        pay.setBasicPay(30000.0);
        pay.setGratuity(5000.0);
        pay.setHra(8000.0);
        pay.setTravelAllowance(2000.0);
        pay.setMealAllowance(1000.0);
        pay.setMedicalAllowance(2000.0);
        pay.setProvidentFund(3000.0);
        pay.setEmployee(employee);

        when(payService.getPayForEmployee(employeeId)).thenReturn(pay);

        ResponseEntity<ApiResponse> response = employeeController.getPay(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(pay, response.getBody().getData());
    }

    @Test
    public void testGetPay_EmployeeNotFound() {
        Long employeeId = 1L;
        when(payService.getPayForEmployee(employeeId)).thenThrow(new CustomException("Pay not found"));

        Exception exception = assertThrows(CustomException.class, () -> {
            employeeController.getPay(employeeId);
        });

        assertEquals("Pay not found", exception.getMessage());
    }
}
