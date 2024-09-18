package com.equifax.ems.service;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.repository.BonusRepository;
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

public class BonusServiceTest {

    @InjectMocks
    private BonusService bonusService;

    @Mock
    private BonusRepository bonusRepository;

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
    public void testAddBonus_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(bonusRepository.save(any(Bonus.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bonus bonus = bonusService.addBonus(1L, "Performance Bonus", 1000, new Date());

        assertThat(bonus).isNotNull();
        assertThat(bonus.getBonusName()).isEqualTo("Performance Bonus");
        assertThat(bonus.getAmount()).isEqualTo(1000);
        assertThat(bonus.getEmployee()).isEqualTo(employee);
        verify(bonusRepository, times(1)).save(any(Bonus.class));
    }

    @Test
    public void testAddBonus_EmpNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bonusService.addBonus(1L, "Performance Bonus", 1000, new Date()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Employee not found");
    }

    @Test
    public void testAddBonus_Failure() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));
        when(bonusRepository.save(any(Bonus.class))).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> bonusService.addBonus(1L, "Performance Bonus", 1000, new Date()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Error adding bonus");
    }

    @Test
    public void testGetBonusesForEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        List<Bonus> bonuses = new ArrayList<>();
        bonuses.add(new Bonus());
        when(bonusRepository.findByEmployeeAndDateBetween(any(Employee.class), any(Date.class), any(Date.class)))
                .thenReturn(bonuses);

        List<Bonus> result = bonusService.getBonusesForEmployee(1L, new Date(System.currentTimeMillis() - 100000), new Date());

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        verify(bonusRepository, times(1)).findByEmployeeAndDateBetween(any(Employee.class), any(Date.class), any(Date.class));
    }

    @Test
    public void testGetBonusesForEmployee_Failure() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bonusService.getBonusesForEmployee(1L, new Date(System.currentTimeMillis() - 100000), new Date()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Employee not found");
    }
}
