package com.techacademy.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(Model model) {

        model.addAttribute("listSize", reportService.findAll().size());
        model.addAttribute("reportList", reportService.findAll());

        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));

        return "reports/detail";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        Employee employee = userDetail.getEmployee();
        String employeeName = employee.getName();

        model.addAttribute("employeeName", employeeName);
        return "reports/new";
    }

    @PostMapping(value = "/add")
    public String add(@ModelAttribute @Validated Report report, BindingResult res,
            @AuthenticationPrincipal UserDetail userDetail, Model model) {
        if (res.hasErrors()) {
            return create(report, userDetail, model);
        }

        Employee employee = userDetail.getEmployee();

        Optional<String> errorMessage = reportService.save(report, employee);

        if (errorMessage.isPresent()) {
            model.addAttribute("errorMessage", errorMessage.get());
            return "reports/new";
        }
        return "redirect:/reports";
    }


    // 日報削除処理

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return detail(id, model);
        }

        return "redirect:/reports";
    }

    // 日報更新処理 画面遷移
    @GetMapping(value = "/{id}/update")
    public String getReport(@PathVariable("id") Integer id, Model model, Report report) {
        if (id == null) {
            model.addAttribute("report", report);
            return "reports/update";
        }
        model.addAttribute("report", reportService.getReport(id));
        return "reports/update";
    }


    @PostMapping("/{id}/update")
    public String postReport(@Validated Report report, BindingResult res, Model model) {
        if (res.hasErrors()) {
            return getReport(report.getId(), model, report);
        }

        Optional<String> errorMessage = reportService.update(report);

        if (errorMessage.isPresent()) {
            model.addAttribute("errorMessage", errorMessage.get());
            return getReport(null, model, report);
        }

        return "redirect:/reports";
    }
}
