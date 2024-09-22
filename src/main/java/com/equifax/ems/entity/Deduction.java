package com.equifax.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;

@Entity
public class Deduction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long deductionId;

    private String deductionName;

    private double amount;

    private Date date;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public Deduction() {
    }

    public Deduction(Long deductionId, double amount, Date date, Employee employee) {
        this.deductionId = deductionId;
        this.amount = amount;
        this.date = date;
        this.employee = employee;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDeductionName() {
        return deductionName;
    }

    public void setDeductionName(String deductionName) {
        this.deductionName = deductionName;
    }

}