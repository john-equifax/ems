package com.equifax.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.util.Date;

@Entity
public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bonusId;


    private String bonusName;
    private double amount;
    private Date date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public Bonus() {
    }

    public Bonus(Long bonusId, double amount, Date date, Employee employee) {
        this.bonusId = bonusId;
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

    public String getBonusName() {
        return bonusName;
    }

    public void setBonusName(String bonusName) {
        this.bonusName = bonusName;
    }

}
