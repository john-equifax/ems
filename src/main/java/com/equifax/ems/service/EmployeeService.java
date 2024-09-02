package com.equifax.ems.service;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.utility.ValidationException;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_SAVING_EMPLOYEE.getMessage(e.getMessage()));
        }
    }

    public List<Employee> fetchAllEmployees() {
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_FETCHING_EMPLOYEES.getMessage( e.getMessage()));
        }
    }

    public Employee getEmployeeById(Long id) {
        try {
            Optional<Employee> employee = employeeRepository.findById(id);
            return employee.orElseThrow(() -> new ValidationException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(id)));
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_FETCHING_EMPLOYEES.getMessage(e.getMessage()));
        }
    }

    public Employee updateEmployeeById(Long id, Employee employee) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(id);
            if (employeeOptional.isPresent()) {
                Employee originalEmployee = employeeOptional.get();
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
                return employeeRepository.save(originalEmployee);
            } else {
                throw new ValidationException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(id));
            }
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_UPDATING_EMPLOYEE.getMessage(e.getMessage()));
        }
    }

    public String deleteEmployeeById(Long id) {
        try {
            if (employeeRepository.findById(id).isPresent()) {
                employeeRepository.deleteById(id);
                return "Employee deleted successfully";
            } else {
                throw new ValidationException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(id));
            }
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_DELETING_EMPLOYEE.getMessage(e.getMessage()));
        }
    }

    public Bonus addEmployeeBonus(Long empId, String name, double amount, Date date) {
        try {
            return bonusService.addBonus(empId, name, amount, date);
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_ADDING_BONUS.getMessage(e.getMessage()));
        }
    }

    public Deduction addEmployeeDeduction(Long empId, String name, double amount, Date date) {
        try {
            return deductionService.addDeduction(empId, name, amount, date);
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_ADDING_DEDUCTION.getMessage(e.getMessage()));
        }
    }
}
