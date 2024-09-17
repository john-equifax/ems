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
        // Create a new Employee instance
        employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        employee.setEmail("jane.doe@example.com");
        employee.setPhoneNumber("0987654321");
        employee.setHireDate(new Date());
        employee.setSalary(60000);
        employeeRepository.save(employee);

        // Create and save deductions for the employee
        Deduction deduction1 = new Deduction();
        deduction1.setAmount(200);
        deduction1.setDeductionName("Health Insurance");
        deduction1.setDate(new Date(System.currentTimeMillis() - 100000)); // 1 day ago
        deduction1.setEmployee(employee);
        deductionRepository.save(deduction1);

        Deduction deduction2 = new Deduction();
        deduction2.setAmount(300);
        deduction2.setDeductionName("Retirement Plan");
        deduction2.setDate(new Date(System.currentTimeMillis() - 500000)); // 5 days ago
        deduction2.setEmployee(employee);
        deductionRepository.save(deduction2);
    }

    @Test
    @Rollback
    public void testFindByEmployeeAndDateBetween_Success() {
        // Define the date range
        Date startDate = new Date(System.currentTimeMillis() - 200000); // 2 days ago
        Date endDate = new Date(); // now

        // Call the method to test
        List<Deduction> deductions = deductionRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);

        // Assert the results
        assertThat(deductions).hasSize(1);
        assertThat(deductions.get(0).getDeductionName()).isEqualTo("Health Insurance");
    }

    @Test
    @Rollback
    public void testFindByEmployeeAndDateBetween_Failure() {
        // Define a date range that does not match any deductions
        Date startDate = new Date(System.currentTimeMillis() + 100000); // 1 day in the future
        Date endDate = new Date(System.currentTimeMillis() + 200000); // 2 days in the future

        // Call the method to test
        List<Deduction> deductions = deductionRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);

        // Assert that the returned list is empty
        assertThat(deductions).isEmpty();
    }
}
