package com.equifax.ems.service;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.CustomException;
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
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testSaveEmployee_Failure() {
        when(employeeRepository.save(any(Employee.class))).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    public void testFetchAllEmployees_Success() {
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.fetchAllEmployees();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    public void testFetchAllEmployees_Failure() {
        when(employeeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> employeeService.fetchAllEmployees())
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    public void testGetEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getEmployeeId()).isEqualTo(1L);
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEmployeeById_Failure() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    public void testUpdateEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee updatedEmployee = employeeService.updateEmployeeById(1L, employee);

        assertThat(updatedEmployee).isNotNull();
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployeeById_Failure() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployeeById(1L, employee))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    public void testDeleteEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        String result = employeeService.deleteEmployeeById(1L);

        assertThat(result).isEqualTo("Employee deleted successfully");
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEmployeeById_Failure() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployeeById(1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Error deleting employee: 1");
    }

    @Test
    public void testAddEmployeeBonus_Success() {
        Bonus bonus = new Bonus();
        when(bonusService.addBonus(anyLong(), anyString(), anyDouble(), any(Date.class))).thenReturn(bonus);

        Bonus result = employeeService.addEmployeeBonus(1L, "Performance Bonus", 1000.0, new Date());

        assertThat(result).isNotNull();
        verify(bonusService, times(1)).addBonus(anyLong(), anyString(), anyDouble(), any(Date.class));
    }

    @Test
    public void testAddEmployeeBonus_Failure() {
        when(bonusService.addBonus(anyLong(), anyString(), anyDouble(), any(Date.class)))
                .thenThrow(new RuntimeException("Error adding bonus"));

        assertThatThrownBy(() -> employeeService.addEmployeeBonus(1L, "Performance Bonus", 1000.0, new Date()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Error adding bonus");
    }

    @Test
    public void testAddEmployeeDeduction_Success() {
        Deduction deduction = new Deduction();
        when(deductionService.addDeduction(anyLong(), anyString(), anyDouble(), any(Date.class))).thenReturn(deduction);

        Deduction result = employeeService.addEmployeeDeduction(1L, "Health Insurance", 200.0, new Date());

        assertThat(result).isNotNull();
        verify(deductionService, times(1)).addDeduction(anyLong(), anyString(), anyDouble(), any(Date.class));
    }

    @Test
    public void testAddEmployeeDeduction_Failure() {
        when(deductionService.addDeduction(anyLong(), anyString(), anyDouble(), any(Date.class)))
                .thenThrow(new RuntimeException("Error adding deduction"));

        assertThatThrownBy(() -> employeeService.addEmployeeDeduction(1L, "Health Insurance", 200.0, new Date()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Error adding deduction");
    }
}
