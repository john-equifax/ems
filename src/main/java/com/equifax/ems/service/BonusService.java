package com.equifax.ems.service;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.utility.ValidationException;
import com.equifax.ems.repository.BonusRepository;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.utility.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BonusService {

    @Autowired
    private BonusRepository bonusRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Bonus addBonus(Long empId, String name, double amount, Date date) {
        try {
            Employee employee = employeeRepository.findById(empId)
                    .orElseThrow(() -> new ValidationException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId)));

            Bonus bonus = new Bonus();
            bonus.setBonusName(name);
            bonus.setEmployee(employee);
            bonus.setAmount(amount);
            bonus.setDate(date);

            return bonusRepository.save(bonus);
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_ADDING_BONUS.getMessage(e.getMessage()));
        }
    }

    public List<Bonus> getBonusesForEmployee(Long empId, Date startDate, Date endDate) {
        try {
            Employee employee = employeeRepository.findById(empId)
                    .orElseThrow(() -> new ValidationException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId)));
            return bonusRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);
        } catch (Exception e) {
            throw new ValidationException(ErrorMessage.ERROR_FETCHING_BONUSES.getMessage(e.getMessage()));
        }
    }
}
