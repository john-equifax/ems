package com.equifax.ems.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Pay;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.repository.PayRepository;
import com.equifax.ems.utility.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PayServiceTest {

    @InjectMocks
    private PayService payService;

    @Mock
    private PayRepository payRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    private Pay pay;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        pay = new Pay(); // Initialize with necessary fields
        employee = new Employee(); // Initialize with necessary fields
        employee.setId(1L); // Set employee ID
    }

    @Test
    public void testSavePay_Success() {
        // Arrange
        when(payRepository.save(pay)).thenReturn(pay);

        // Act
        payService.savePay(pay);

        // Assert
        verify(payRepository, times(1)).save(pay);
    }

    @Test
    public void testSavePay_RuntimeException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(payRepository).save(pay);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> payService.savePay(pay));
        assertEquals("Error saving pay: Database error", exception.getMessage());
    }

    @Test
    public void testGetPayForEmployee_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(payRepository.findByEmployee(employee)).thenReturn(pay);

        // Act
        Pay result = payService.getPayForEmployee(1L);

        // Assert
        assertEquals(pay, result);
        verify(employeeRepository, times(1)).findById(1L);
        verify(payRepository, times(1)).findByEmployee(employee);
    }

    @Test
    public void testGetPayForEmployee_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> payService.getPayForEmployee(1L));
        assertEquals("Employee not found with id: 1", exception.getMessage());
    }
}
