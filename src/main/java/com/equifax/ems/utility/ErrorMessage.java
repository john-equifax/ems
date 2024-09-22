package com.equifax.ems.utility;

public enum ErrorMessage {
    EMPLOYEE_NOT_FOUND("Employee not found with id: %d"),
    START_DATE_AFTER_END_DATE("Oops! The start date is after the end date."),
    ERROR_FETCHING_EMPLOYEES("Error fetching employees: %s"),
    ERROR_FETCHING_PAY("Error fetching pay: %s"),
    ERROR_SAVING_EMPLOYEE("Error saving employee: %s"),
    ERROR_SAVING_PAY("Error saving pay: %s"),
    ERROR_UPDATING_EMPLOYEE("Error updating employee: %s"),
    ERROR_DELETING_EMPLOYEE("Error deleting employee: %s"),
    ERROR_DELETING_PAY("Error deleting pay: %s"),
    ERROR_ADDING_BONUS("Error adding bonus: %s"),
    ERROR_ADDING_DEDUCTION("Error adding deduction: %s"),
    ERROR_FETCHING_BONUSES("Error fetching bonuses for employee: %s"),
    ERROR_FETCHING_DEDUCTIONS("Error fetching deductions for employee: %s"),
    INVALID_DATE_RANGE("Oops! Invalid date range."),
    ERROR_GENERATING_PAYSLIP("Error generating payslip: %s");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
