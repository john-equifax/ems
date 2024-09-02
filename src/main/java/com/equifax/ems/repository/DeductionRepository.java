package com.equifax.ems.repository;

import com.equifax.ems.entity.Deduction;
import com.equifax.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    List<Deduction> findByEmployeeAndDateBetween(Employee employee, Date startDate, Date endDate);
}
