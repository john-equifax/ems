package com.equifax.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employeeId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Hire date is required")
    private Date hireDate;

    @Positive(message = "Salary must be positive")
    private double salary;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Bonus> bonuses;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Deduction> deductions;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Payslip> payslips;
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Pay pay;


    public Employee() {
    }

    public Employee(Long employeeId, String firstName, String lastName, String email, String phoneNumber, Date hireDate, double salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.salary = salary;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setId(long l) {
    }
}
