package com.equifax.ems.service;

import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.utility.ValidationException;
import com.equifax.ems.repository.DeductionRepository;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DeductionService {

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Deduction addDeduction(Long empId, String name, double amount, Date date) {
        try {
            Employee employee = employeeRepository.findById(empId)
                    .orElseThrow(() -> new ValidationException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId)));

            Deduction deduction = new Deduction();
            deduction.setEmployee(employee);
            deduction.setDeductionName(name);
            deduction.setAmount(amount);
            deduction.setDate(date);

            return deductionRepository.save(deduction);
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_ADDING_DEDUCTION.getMessage(e.getMessage()));
        }
    }

    public List<Deduction> getDeductionForEmployee(Long empId, Date startDate, Date endDate) {
        try {
            Employee employee = employeeRepository.findById(empId)
                    .orElseThrow(() -> new ValidationException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId)));
            return deductionRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_FETCHING_DEDUCTIONS.getMessage( e.getMessage()));
        }
    }
}
