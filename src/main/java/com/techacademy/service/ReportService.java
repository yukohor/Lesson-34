package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportrepository) {
        this.reportRepository = reportrepository;
    }

    public List<Report> findByEmployee(Employee employee) {
        return reportRepository.findByEmployee(employee);
    }

     //新規
    @Transactional
    public Optional<String> save(Report report, Employee employee) {
        Optional<Report> existingReport = reportRepository.findByEmployeeAndReportDate(employee, report.getReportDate());

        if (existingReport.isPresent()) {
            return Optional.of("この日付の日報は既に存在します。");
        }

        report.setEmployee(employee);
        reportRepository.save(report);

        return Optional.empty();
    }


    // 日報削除
    @Transactional
    public ErrorKinds delete(Integer id, UserDetail userDetail) {
        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;

    }

    // 日報更新
    @Transactional
    public ErrorKinds update(Report report) {

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;

    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 更新用
    public Report getReport(Integer id) {
        return reportRepository.findById(id).get();
    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }
}
