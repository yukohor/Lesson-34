package com.techacademy.controller;

import org.springframework.ui.Model;

@Controller
 @RequestMapping("reports")
 public class ReportController {

     private final ReportsService reportService;

     @Autowired
     public ReportController(ReportService reportService) {
         this.reportService = reportService;
     }


     //日報一覧画面
     @GetMapping
     public String list(Model model) {
         model.addAttribute("listSize", reportService.findAll().size());
         model.addAttribute("reportList", reportService.findAll());

         return "reports/list";
     }

     //日報詳細画面
     @GetMapping(value = "/{id}/")
     public String detail(@PathVariable String code, Model model) {
         model.addAttribute("report", reportService.findById(id));
         return "reports/detail";
     }

     //日報新規登録画面
     @GetMapping(value = "/add")
     public String create(@ModelAttribute Report report) {
         return "reports/new";
     }

     //日報新規登録処理　8/27~

}
