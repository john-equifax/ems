package com.equifax.ems.entity;

import jakarta.persistence.*;

import java.util.Date;


@Entity
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long payslipId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Date generateDate;
    private Date startDate;

    public Date getEndDate() {
        return endDate;
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

    private Date endDate;
    private double tax;
    private double netPay;

    public Payslip() {
    }

    //Setter and getter for Payslip ID
    public Long getPayslipId() {
        return payslipId;
    }

    public void setPayslipId(Long payslipId) {
        this.payslipId = payslipId;
    }

    //Setter and getter for Payslip generated DATE
    public Date getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Date generateDate) {
        this.generateDate = generateDate;
    }

    //Setter and getter for Payslip TAX
    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    //Setter and getter for Payslip NETPAY
    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    //Setter and getter for Payslip employee
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
