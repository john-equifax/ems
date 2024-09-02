package com.equifax.ems.utility;

public enum ErrorMessage {
    EMPLOYEE_NOT_FOUND("Employee not found with id: %d"),
    BONUS_NOT_FOUND("Bonus not found with id: %d"),
    DEDUCTION_NOT_FOUND("Deduction not found with id: %d"),
    PAYSLIP_NOT_FOUND("Payslip not found for employee with id: %d"),
    START_DATE_AFTER_END_DATE("Oops! The start date is after the end date."),
    ERROR_FETCHING_EMPLOYEES("Error fetching employees: %s"),
    ERROR_SAVING_EMPLOYEE("Error saving employee: %s"),
    ERROR_UPDATING_EMPLOYEE("Error updating employee: %s"),
    ERROR_DELETING_EMPLOYEE("Error deleting employee: %s"),
    ERROR_ADDING_BONUS("Error adding bonus: %s"),
    ERROR_ADDING_DEDUCTION("Error adding deduction: %s"),
    ERROR_FETCHING_BONUSES("Error fetching bonuses for employee: %s"),
    ERROR_FETCHING_DEDUCTIONS("Error fetching deductions for employee: %s"),
    ERROR_GENERATING_PAYSLIP("Error generating payslip: %s"),
    ERROR_FETCHING_PAYSLIPS("Error fetching all payslips: %s");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
