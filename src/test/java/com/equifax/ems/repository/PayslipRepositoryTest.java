package com.equifax.ems.repository;

import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Payslip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PayslipRepositoryTest {

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        // Create a new Employee instance
        employee = new Employee();
        employee.setFirstName("Alice");
        employee.setLastName("Smith");
        employee.setEmail("alice.smith@example.com");
        employee.setPhoneNumber("1234567890");
        employee.setHireDate(new Date());
        employee.setSalary(70000);
        employeeRepository.save(employee);

        // Create and save payslips for the employee
        Payslip payslip1 = new Payslip();
        payslip1.setEmployee(employee);
        payslip1.setGenerateDate(new Date());
        payslip1.setStartDate(new Date(System.currentTimeMillis() - 2592000000L)); // 30 days ago
        payslip1.setEndDate(new Date(System.currentTimeMillis() - 2592000000L + 604800000L)); // 30 days ago + 7 days
        payslip1.setTax(150);
        payslip1.setNetPay(2000);
        payslipRepository.save(payslip1);

        Payslip payslip2 = new Payslip();
        payslip2.setEmployee(employee);
        payslip2.setGenerateDate(new Date());
        payslip2.setStartDate(new Date(System.currentTimeMillis() - 5184000000L)); // 60 days ago
        payslip2.setEndDate(new Date(System.currentTimeMillis() - 5184000000L + 604800000L)); // 60 days ago + 7 days
        payslip2.setTax(200);
        payslip2.setNetPay(2500);
        payslipRepository.save(payslip2);
    }

    @Test
    @Rollback
    public void testFindPayslipByEmployeeAndDateRange_Success() {
        // Define the date range
        Date startDate = new Date(System.currentTimeMillis() - 3000000000L); // 35 days ago
        Date endDate = new Date(); // now

        // Call the method to test
        Optional<Payslip> payslip = payslipRepository.findPayslipByEmployeeAndDateRange(employee.getEmployeeId(), startDate, endDate);

        // Assert the results
        assertThat(payslip).isPresent();
        assertThat(payslip.get().getNetPay()).isEqualTo(2000);
    }

    @Test
    @Rollback
    public void testFindPayslipByEmployeeAndDateRange_Failure() {
        // Define a date range that does not match any payslips
        Date startDate = new Date(System.currentTimeMillis() + 100000); // 1 day in the future
        Date endDate = new Date(System.currentTimeMillis() + 200000); // 2 days in the future

        // Call the method to test
        Optional<Payslip> payslip = payslipRepository.findPayslipByEmployeeAndDateRange(employee.getEmployeeId(), startDate, endDate);

        // Assert that the returned Optional is empty
        assertThat(payslip).isNotPresent();
    }
}
