package com.equifax.ems.utility;

import com.equifax.ems.entity.Pay;

public class UtilityMethods {
    public static double calculateTaxSlab(double income) {
        double taxPercentage = 0.0;

        // Determine tax percentage based on income slabs
        if (income <= 250000) {
            taxPercentage = 0; // No tax
        } else if (income <= 500000) {
            taxPercentage = 5; // 5% tax
        } else if (income <= 750000) {
            taxPercentage = 10; // 10% tax
        } else if (income <= 1000000) {
            taxPercentage = 15; // 15% tax
        } else if (income <= 1250000) {
            taxPercentage = 20; // 20% tax
        } else if (income <= 1500000) {
            taxPercentage = 25; // 25% tax
        } else {
            taxPercentage = 30; // 30% tax
        }
        return taxPercentage;
    }
    public static Pay createPay(Double salary) {
        Pay pay = new Pay();
        pay.setBasicPay(0.45 * salary);
        pay.setGratuity(0.05 * salary);
        pay.setHra(0.20 * salary);
        pay.setTravelAllowance(0.07 * salary);
        pay.setMealAllowance(0.03 * salary);
        pay.setMedicalAllowance(0.08 * salary);
        pay.setProvidentFund(0.12 * salary);
        return pay; // Just return the Pay object without saving it
    }
}
