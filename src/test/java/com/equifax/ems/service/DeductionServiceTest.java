package com.equifax.ems.service;

import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.repository.DeductionRepository;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeductionServiceTest {

    @InjectMocks
    private DeductionService deductionService;

    @Mock
    private DeductionRepository deductionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumber("1234567890");
        employee.setHireDate(new Date());
        employee.setSalary(50000);
    }

    @Test
    public void testAddDeduction_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(deductionRepository.save(any(Deduction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Deduction deduction = deductionService.addDeduction(1L, "Health Insurance", 200, new Date());

        // Assert
        assertThat(deduction).isNotNull();
        assertThat(deduction.getDeductionName()).isEqualTo("Health Insurance");
        assertThat(deduction.getAmount()).isEqualTo(200);
        assertThat(deduction.getEmployee()).isEqualTo(employee);
        verify(deductionRepository, times(1)).save(any(Deduction.class));
    }

    @Test
    public void testAddDeduction_Failure() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deductionService.addDeduction(1L, "Health Insurance", 200, new Date()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    public void testGetDeductionForEmployee_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        List<Deduction> deductions = new ArrayList<>();
        deductions.add(new Deduction());
        when(deductionRepository.findByEmployeeAndDateBetween(any(Employee.class), any(Date.class), any(Date.class)))
                .thenReturn(deductions);

        // Act
        List<Deduction> result = deductionService.getDeductionForEmployee(1L, new Date(System.currentTimeMillis() - 100000), new Date());

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(deductionRepository, times(1)).findByEmployeeAndDateBetween(any(Employee.class), any(Date.class), any(Date.class));
    }

    @Test
    public void testGetDeductionForEmployee_Failure() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deductionService.getDeductionForEmployee(1L, new Date(System.currentTimeMillis() - 100000), new Date()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Employee not found");
    }
}
