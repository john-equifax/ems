package com.equifax.ems.service;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.ValidationException;
import com.equifax.ems.utility.ErrorMessage;
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

public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BonusService bonusService;

    @Mock
    private DeductionService deductionService;

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
        employee.setSalary(50000);
    }

    @Test
    public void testSaveEmployee_Success() {
        // Arrange
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Assert
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testSaveEmployee_Failure() {
        // Arrange
        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    public void testFetchAllEmployees_Success() {
        // Arrange
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.fetchAllEmployees();

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testFetchAllEmployees_Failure() {
        // Arrange
        when(employeeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> employeeService.fetchAllEmployees())
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    public void testGetEmployeeById_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.getEmployeeById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(1L);
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEmployeeById_Failure() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    public void testUpdateEmployeeById_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee updatedEmployee = employeeService.updateEmployeeById(1L, employee);

        // Assert
        assertThat(updatedEmployee).isNotNull();
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeById_Failure() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.updateEmployeeById(1L, employee))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    public void testDeleteEmployeeById_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        String result = employeeService.deleteEmployeeById(1L);

        // Assert
        assertThat(result).isEqualTo("Employee deleted successfully");
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEmployeeById_Failure() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.deleteEmployeeById(1L))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    public void testAddEmployeeBonus_Success() {
        // Arrange
        Bonus bonus = new Bonus();
        when(bonusService.addBonus(anyLong(), anyString(), anyDouble(), any(Date.class))).thenReturn(bonus);

        // Act
        Bonus result = employeeService.addEmployeeBonus(1L, "Performance Bonus", 1000, new Date());

        // Assert
        assertThat(result).isNotNull();
        verify(bonusService, times(1)).addBonus(anyLong(), anyString(), anyDouble(), any(Date.class));
    }

    @Test
    public void testAddEmployeeBonus_Failure() {
        // Arrange
        when(bonusService.addBonus(anyLong(), anyString(), anyDouble(), any(Date.class)))
                .thenThrow(new RuntimeException("Error adding bonus"));

        // Act & Assert
        assertThatThrownBy(() -> employeeService.addEmployeeBonus(1L, "Performance Bonus", 1000, new Date()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Error adding bonus");
    }

    @Test
    public void testAddEmployeeDeduction_Success() {
        // Arrange
        Deduction deduction = new Deduction();
        when(deductionService.addDeduction(anyLong(), anyString(), anyDouble(), any(Date.class))).thenReturn(deduction);

        // Act
        Deduction result = employeeService.addEmployeeDeduction(1L, "Health Insurance", 200, new Date());

        // Assert
        assertThat(result).isNotNull();
        verify(deductionService, times(1)).addDeduction(anyLong(), anyString(), anyDouble(), any(Date.class));
    }

    @Test
    public void testAddEmployeeDeduction_Failure() {
        // Arrange
        when(deductionService.addDeduction(anyLong(), anyString(), anyDouble(), any(Date.class)))
                .thenThrow(new RuntimeException("Error adding deduction"));

        // Act & Assert
        assertThatThrownBy(() -> employeeService.addEmployeeDeduction(1L, "Health Insurance", 200, new Date()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Error adding deduction");
    }
}
