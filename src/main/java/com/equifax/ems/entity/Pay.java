package com.equifax.ems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long payId;

    @Positive(message = "Salary must be positive")
    private double basicPay;
    @Positive(message = "Gratuity must be positive")
    private double gratuity;
    @Positive(message = "HRA must be positive")
    private double hra;
    @Positive(message = "Travel Allowance must be positive")
    private double travelAllowance;
    @Positive(message = "Meal Allowance must be positive")
    private double mealAllowance;
    @Positive(message = "Medical Allowance must be positive")
    private double medicalAllowance;
    @Positive(message = "Provident Fund must be positive")
    private double providentFund;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public Pay() {
    }

    public Pay(Long payId, double basicPay, double gratuity, double hra, double travelAllowance, double mealAllowance, double medicalAllowance, double providentFund, Employee employee) {
        this.payId = payId;
        this.basicPay = basicPay;
        this.gratuity = gratuity;
        this.hra = hra;
        this.travelAllowance = travelAllowance;
        this.mealAllowance = mealAllowance;
        this.medicalAllowance = medicalAllowance;
        this.providentFund = providentFund;
        this.employee = employee;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public double getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(double providentFund) {
        this.providentFund = providentFund;
    }

    public double getMedicalAllowance() {
        return medicalAllowance;
    }

    public void setMedicalAllowance(double medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public double getMealAllowance() {
        return mealAllowance;
    }

    public void setMealAllowance(double mealAllowance) {
        this.mealAllowance = mealAllowance;
    }

    public double getTravelAllowance() {
        return travelAllowance;
    }

    public void setTravelAllowance(double travelAllowance) {
        this.travelAllowance = travelAllowance;
    }

    public double getHra() {
        return hra;
    }

    public void setHra(double hra) {
        this.hra = hra;
    }

    public double getGratuity() {
        return gratuity;
    }

    public void setGratuity(double gratuity) {
        this.gratuity = gratuity;
    }

    public double getBasicPay() {
        return basicPay;
    }

    public void setBasicPay(double basicPay) {
        this.basicPay = basicPay;
    }
}
