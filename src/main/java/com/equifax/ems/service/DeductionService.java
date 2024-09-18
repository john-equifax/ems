package com.equifax.ems.service;

import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.repository.DeductionRepository;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.CustomException;
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
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId)));
        try {
            Deduction deduction = new Deduction();
            deduction.setEmployee(employee);
            deduction.setDeductionName(name);
            deduction.setAmount(amount);
            deduction.setDate(date);
            return deductionRepository.save(deduction);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_ADDING_DEDUCTION.getMessage(e.getMessage()));
        }
    }


    public List<Deduction> getDeductionForEmployee(Long empId, Date startDate, Date endDate) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId)));
        return deductionRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);

    }
}
