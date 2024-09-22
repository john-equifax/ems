package com.equifax.ems.service;

import com.equifax.ems.entity.*;
import com.equifax.ems.repository.PayslipRepository;
import com.equifax.ems.utility.CustomException;
import com.equifax.ems.utility.ErrorMessage;
import com.equifax.ems.utility.UtilityMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PayslipService {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PayslipRepository payslipRepository;
    @Autowired
    private BonusService bonusService;
    @Autowired
    private DeductionService deductionService;
    @Autowired
    private PayService payService;

    public List<Payslip> fetchAllPayslips() {
        return payslipRepository.findAll();
    }

    public Payslip generatepayslip(Long empId, Date startDate, Date endDate) {
        Employee employee = employeeService.getEmployeeById(empId);
        if (employee == null) {
            throw new CustomException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId));
        }

        // Convert the given Dates to LocalDate
        LocalDate hireLocalDate = employee.getHireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (startLocalDate.isAfter(endLocalDate)) {
            throw new CustomException(ErrorMessage.START_DATE_AFTER_END_DATE.getMessage());
        }

        // Calculate the number of days between the two given dates
        long daysInThePeriod;
        if (hireLocalDate.getYear() == startLocalDate.getYear() && hireLocalDate.getMonth() == startLocalDate.getMonth() && hireLocalDate.isBefore(endLocalDate)) {
            daysInThePeriod = ChronoUnit.DAYS.between(hireLocalDate, endLocalDate);
        } else {
            daysInThePeriod = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        }

        // Validate the number of days in the period
        if (daysInThePeriod < 28 || daysInThePeriod > 31) {
            throw new CustomException(ErrorMessage.INVALID_DATE_RANGE.getMessage());
        }

        Optional<Payslip> existingPayslip = payslipRepository.findPayslipByEmployeeAndDateRange(empId, startDate, endDate);
        if (existingPayslip.isPresent()) {
            return existingPayslip.get();
        }

        List<Bonus> bonuses;
        List<Deduction> deductions;
        Pay pay;

        try {
            bonuses = bonusService.getBonusesForEmployee(empId, startDate, endDate);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_FETCHING_BONUSES.getMessage(e.getMessage()));
        }

        try {
            deductions = deductionService.getDeductionForEmployee(empId, startDate, endDate);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_FETCHING_DEDUCTIONS.getMessage(e.getMessage()));
        }

        try {
            pay = payService.getPayForEmployee(empId);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_FETCHING_PAY.getMessage(e.getMessage()));
        }

        double totalBonuses = bonuses.stream().mapToDouble(Bonus::getAmount).sum();
        double totalDeductions = deductions.stream().mapToDouble(Deduction::getAmount).sum();

        double netPayYearly = pay.getBasicPay() + pay.getHra() + pay.getTravelAllowance() + pay.getMealAllowance() + pay.getMedicalAllowance() - pay.getProvidentFund() - pay.getGratuity();
        double netPayDaily = netPayYearly / 365;

        double paymentForPeriod = daysInThePeriod * netPayDaily;
        double netAmount = paymentForPeriod + totalBonuses - totalDeductions;

        double taxPercentage = UtilityMethods.calculateTaxSlab(netPayYearly);
        double taxAmount = netAmount * taxPercentage / 100;
        double netPay = netAmount - taxAmount;

        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setGenerateDate(new Date());
        payslip.setStartDate(startDate);
        payslip.setEndDate(endDate);
        payslip.setTax(taxAmount);
        payslip.setNetPay(netPay);
        try {
            return payslipRepository.save(payslip);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_GENERATING_PAYSLIP.getMessage(e.getMessage()));
        }
    }

    public List<Payslip> generatePayslipAll( Date startDate, Date endDate){
        List<Employee> employees = employeeService.fetchAllEmployees();
        List<Payslip> payslips = new ArrayList<>();
        for(Employee employee :employees)
        {
            Payslip payslip = generatepayslip(employee.getEmployeeId(),startDate,endDate);
            payslips.add(payslip);
        }
        return payslips;
    }
}
