package com.equifax.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;


@Entity
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long payslipId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Date generateDate;
    private Date startDate;
    private Date endDate;
    private double tax;
    private double netPay;


    public Payslip() {
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setPayslipId(Long payslipId) {
        this.payslipId = payslipId;
    }


    public void setGenerateDate(Date generateDate) {
        this.generateDate = generateDate;
    }


    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }


    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
