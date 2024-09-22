package com.equifax.ems.controller;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Pay;
import com.equifax.ems.service.BonusService;
import com.equifax.ems.service.DeductionService;
import com.equifax.ems.service.EmployeeService;
import com.equifax.ems.service.PayService;
import com.equifax.ems.utility.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private BonusService bonusService;
    @Autowired
    private DeductionService deductionService;
    @Autowired
    private PayService payService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllEmployees() {
        List<Employee> employees = employeeService.fetchAllEmployees();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), employees, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), employee, null));
    }

    @GetMapping("/{id}/bonuses")
    public ResponseEntity<ApiResponse> getBonusesByIdAndDate(@PathVariable("id") Long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Bonus> bonuses = bonusService.getBonusesForEmployee(id, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), bonuses, null));
    }

    @GetMapping("/{id}/deductions")
    public ResponseEntity<ApiResponse> getDeductionsByIdAndDate(@PathVariable("id") Long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Deduction> deductions = deductionService.getDeductionForEmployee(id, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), deductions, null));
    }

    @GetMapping("/{id}/pay")
    public ResponseEntity<ApiResponse> getPay(@PathVariable("id") Long id) {
        Pay pay = payService.getPayForEmployee(id);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),pay, null));
    }

    @PostMapping("/new")
    public ResponseEntity<ApiResponse> saveEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), savedEmployee, null));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployeeById(id, employee);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), updatedEmployee, null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable("id") Long id) {
        String message = employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), message, null));
    }

    @PostMapping("/{id}/bonus/add")
    public ResponseEntity<ApiResponse> addEmployeeBonus(@PathVariable("id") Long id,
                                                        @RequestParam String name,
                                                        @RequestParam double amount,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        Bonus bonus = employeeService.addEmployeeBonus(id, name, amount, date);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), bonus, null));
    }

    @PostMapping("/{id}/deduction/add")
    public ResponseEntity<ApiResponse> addEmployeeDeduction(@PathVariable("id") Long id,
                                                            @RequestParam String name,
                                                            @RequestParam double amount,
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        Deduction deduction = employeeService.addEmployeeDeduction(id, name, amount, date);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), deduction, null));
    }
}
