package com.equifax.ems.service;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.CustomException;
import com.equifax.ems.utility.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BonusService bonusService;
    @Autowired
    private DeductionService deductionService;

    public Employee saveEmployee(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_SAVING_EMPLOYEE.getMessage(e.getMessage()));
        }
    }

    public List<Employee> fetchAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_FETCHING_EMPLOYEES.getMessage(e.getMessage()));
        }
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(id)));
    }

    public Employee updateEmployeeById(Long id, Employee employee) {
        Employee originalEmployee = getEmployeeById(id);
        if (Objects.nonNull(employee.getFirstName()) && !"".equalsIgnoreCase(employee.getFirstName())) {
            originalEmployee.setFirstName(employee.getFirstName());
        }
        if (Objects.nonNull(employee.getLastName()) && !"".equalsIgnoreCase(employee.getLastName())) {
            originalEmployee.setLastName(employee.getLastName());
        }
        if (Objects.nonNull(employee.getEmail()) && !"".equalsIgnoreCase(employee.getEmail())) {
            originalEmployee.setEmail(employee.getEmail());
        }
        if (Objects.nonNull(employee.getPhoneNumber()) && !"".equalsIgnoreCase(employee.getPhoneNumber())) {
            originalEmployee.setPhoneNumber(employee.getPhoneNumber());
        }
        if (employee.getSalary() != 0) {
            originalEmployee.setSalary(employee.getSalary());
        }
        try {
            return employeeRepository.save(originalEmployee);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_UPDATING_EMPLOYEE.getMessage(id));
        }

    }

    public String deleteEmployeeById(Long id) {
        if (employeeRepository.findById(id).isPresent()) {
            employeeRepository.deleteById(id);
            return "Employee deleted successfully";
        } else {
            throw new CustomException(ErrorMessage.ERROR_DELETING_EMPLOYEE.getMessage(id));
        }
    }

    public Bonus addEmployeeBonus(Long employeeId, String bonusName, Double amount, Date date) {
        try {
            return bonusService.addBonus(employeeId, bonusName, amount, date);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_ADDING_BONUS.getMessage(e.getMessage()));
        }
    }

    public Deduction addEmployeeDeduction(Long employeeId, String deductionName, Double amount, Date date) {
        try {
            return deductionService.addDeduction(employeeId, deductionName, amount, date);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_ADDING_DEDUCTION.getMessage(e.getMessage()));
        }
    }
}
