package com.equifax.ems.service;

import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Pay;
import com.equifax.ems.repository.EmployeeRepository;
import com.equifax.ems.repository.PayRepository;
import com.equifax.ems.utility.CustomException;
import com.equifax.ems.utility.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {
    @Autowired
    private PayRepository payRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    public void savePay(Pay pay) {
        try {
            payRepository.save(pay);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorMessage.ERROR_SAVING_PAY.getMessage(e.getMessage()));
        }
    }

//    public void deletePayById(Long id) {
//        if (payRepository.findById(id).isPresent()) {
//            payRepository.deleteById(id);
//        } else {
//            throw new CustomException(ErrorMessage.ERROR_DELETING_PAY.getMessage(id));
//        }
//    }

//    public List<Pay> fetchAllPays() {
//        try {
//            return payRepository.findAll();
//        } catch (RuntimeException e) {
//            throw new CustomException(ErrorMessage.ERROR_FETCHING_PAY.getMessage(e.getMessage()));
//        }
//    }

    public Pay getPayForEmployee(Long empId) {
        Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new CustomException(ErrorMessage.EMPLOYEE_NOT_FOUND.getMessage(empId)));
        return payRepository.findByEmployee(employee);
    }


}
