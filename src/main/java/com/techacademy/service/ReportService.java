package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportrepository) {
        this.reportRepository = reportrepository;
    }

    // 日報保存
    @Transactional
    public Report save(Report report) {
        return reportRepository.save(report);

        /*
         * public ErrorKinds save(Report report) { report.setDeleteFlg(false);
         *
         * LocalDateTime now = LocalDateTime.now(); report.setCreatedAt(now);
         * report.setUpdatedAt(now); reportRepository.save(report); return
         * ErrorKinds.SUCCESS;
         */
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
    public Report update(Report report) {
        return reportRepository.save(report);
        /*
         * public ErrorKinds update(Report report) { report.setDeleteFlg(false);
         *
         * LocalDateTime now = LocalDateTime.now(); report.setCreatedAt(now);
         * report.setUpdatedAt(now);
         *
         * reportRepository.save(report); return ErrorKinds.SUCCESS;
         */
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

   //更新用
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

    /*// ログイン情報取得
    public Report getLoggedInName() {
        SecurityContextHolder.getContext().getAuthentication().getName();

        // DB等からユーザ情報を取得する処理
        return getReport(null);
    }*/
}
