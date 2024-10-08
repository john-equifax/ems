package com.equifax.ems.repository;

import com.equifax.ems.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {

    @Query("SELECT p FROM Payslip p WHERE p.employee.employeeId = :employeeId AND " +
            "((p.startDate BETWEEN :startDate AND :endDate) OR " +
            "(p.endDate BETWEEN :startDate AND :endDate))")
    Optional<Payslip> findPayslipByEmployeeAndDateRange(@Param("employeeId") Long employeeId,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate);
}
