package com.equifax.ems.controller;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.service.BonusService;
import com.equifax.ems.service.DeductionService;
import com.equifax.ems.utility.ApiResponse;
import com.equifax.ems.service.EmployeeService;
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

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllEmployees() {
        try {
            List<Employee> employees = employeeService.fetchAllEmployees();
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), employees, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable("id") Long id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), employee, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }

    @GetMapping("/{id}/bonuses")
    public ResponseEntity<ApiResponse> getBonusesByIdAndDate(@PathVariable("id") Long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Bonus> bonuses = bonusService.getBonusesForEmployee(id,startDate,endDate);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), bonuses, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }

    @GetMapping("/{id}/deductions")
    public ResponseEntity<ApiResponse> getDeductionsByIdAndDate(@PathVariable("id") Long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<Deduction> deductions = deductionService.getDeductionForEmployee(id,startDate,endDate);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), deductions, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }

    @PostMapping("/new")
    public ResponseEntity<ApiResponse> saveEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(HttpStatus.CREATED.value(), savedEmployee, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        try {
            Employee updatedEmployee = employeeService.updateEmployeeById(id, employee);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), updatedEmployee, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable("id") Long id) {
        try {
            String message = employeeService.deleteEmployeeById(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), message, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), null, e.getMessage()));
        }
    }

    @PostMapping("/{id}/bonus/add")
    public ResponseEntity<ApiResponse> addEmployeeBonus(@PathVariable("id") Long id,
                                                        @RequestParam String name,
                                                        @RequestParam double amount,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            Bonus bonus = employeeService.addEmployeeBonus(id, name, amount, date);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(HttpStatus.CREATED.value(), bonus, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
        }
    }

    @PostMapping("/{id}/deduction/add")
    public ResponseEntity<ApiResponse> addEmployeeDeduction(@PathVariable("id") Long id,
                                                            @RequestParam String name,
                                                            @RequestParam double amount,
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        try {
            Deduction deduction = employeeService.addEmployeeDeduction(id, name, amount, date);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(HttpStatus.CREATED.value(), deduction, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage()));
        }
    }
}
