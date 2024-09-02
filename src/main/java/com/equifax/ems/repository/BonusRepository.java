package com.equifax.ems.repository;

import com.equifax.ems.entity.Bonus;
import com.equifax.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {
    List<Bonus> findByEmployeeAndDateBetween(Employee employee, Date startDate, Date endDate);
}
