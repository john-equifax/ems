package com.equifax.ems.repository;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Employee;
import com.equifax.ems.entity.Pay;
import com.equifax.ems.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {
   Pay findByEmployee(Employee employee);
}
