package com.equifax.ems.repository;

import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeductionRepositoryTest {

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        employee.setEmail("jane.doe@example.com");
        employee.setPhoneNumber("0987654321");
        employee.setHireDate(new Date());
        employee.setSalary(60000);
        employeeRepository.save(employee);

        Deduction deduction1 = new Deduction();
        deduction1.setAmount(200);
        deduction1.setDeductionName("Health Insurance");
        deduction1.setDate(new Date(System.currentTimeMillis() - 100000));
        deduction1.setEmployee(employee);
        deductionRepository.save(deduction1);

        Deduction deduction2 = new Deduction();
        deduction2.setAmount(300);
        deduction2.setDeductionName("Retirement Plan");
        deduction2.setDate(new Date(System.currentTimeMillis() - 500000));
        deduction2.setEmployee(employee);
        deductionRepository.save(deduction2);
    }

    @Test
    @Rollback
    public void testFindByEmployeeAndDateBetween_Success() {
        Date startDate = new Date(System.currentTimeMillis() - 200000);
        Date endDate = new Date();
        List<Deduction> deductions = deductionRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);

        assertThat(deductions).hasSize(1);
        assertThat(deductions.get(0).getDeductionName()).isEqualTo("Health Insurance");
    }

    @Test
    @Rollback
    public void testFindByEmployeeAndDateBetween_Failure() {
        Date startDate = new Date(System.currentTimeMillis() + 100000);
        Date endDate = new Date(System.currentTimeMillis() + 200000);
        List<Deduction> deductions = deductionRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);

        assertThat(deductions).isEmpty();
    }
}
