package com.equifax.ems.repository;

import com.equifax.ems.entity.Bonus;
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
public class BonusRepositoryTest {

    @Autowired
    private BonusRepository bonusRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        // Create a new Employee instance
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumber("1234567890");
        employee.setHireDate(new Date());
        employee.setSalary(50000);
        employeeRepository.save(employee);

        // Create and save bonuses for the employee
        Bonus bonus1 = new Bonus();
        bonus1.setAmount(1000);
        bonus1.setBonusName("Performance Bonus");
        bonus1.setDate(new Date(System.currentTimeMillis() - 100000)); // 1 day ago
        bonus1.setEmployee(employee);
        bonusRepository.save(bonus1);

        Bonus bonus2 = new Bonus();
        bonus2.setAmount(1500);
        bonus2.setBonusName("Year-End Bonus");
        bonus2.setDate(new Date(System.currentTimeMillis() - 500000)); // 5 days ago
        bonus2.setEmployee(employee);
        bonusRepository.save(bonus2);

    }

    @Test
    @Rollback
    public void testFindByEmployeeAndDateBetween_Success() {

        // Define the date range
        Date startDate = new Date(System.currentTimeMillis() - 200000); // 2 days ago
        Date endDate = new Date(); // now

        // Call the method to test
        List<Bonus> bonuses = bonusRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);

        // Assert the results
        assertThat(bonuses).hasSize(1);
        assertThat(bonuses.get(0).getBonusName()).isEqualTo("Performance Bonus");
    }

    @Test
    @Rollback
    public void testFindByEmployeeAndDateBetween_Failure() {


        // Define a date range that does not match any bonuses
        Date startDate = new Date(System.currentTimeMillis() + 100000); // 1 day in the future
        Date endDate = new Date(System.currentTimeMillis() + 200000); // 2 days in the future

        // Call the method to test
        List<Bonus> bonuses = bonusRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);

        // Assert that the returned list is empty
        assertThat(bonuses).isEmpty();
    }

}
