package com.techacademy.repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;


public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByEmployee(Employee employee);
    Optional<Report> findByEmployeeAndReportDate(Employee employee, LocalDate reportDate);

}
