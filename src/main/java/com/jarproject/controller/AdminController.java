package com.jarproject.controller;

import com.jarproject.entity.Student;
import com.jarproject.service.statistic.StatisticService;
import com.jarproject.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController{
    @Autowired
    private StudentService studentService;
    @Autowired
    private HttpSession session;
    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public String admin(){
        return "redirect:/admin/dashboard";
    }

    @GetMapping("dashboard")
    public String dashboard(Model model){
        Integer curUser = (Integer) session.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
            return "redirect:/login";
        }

        model.addAttribute("studentCount", statisticService.countAllStudent());
        model.addAttribute("courseCount", statisticService.countAllCourse());
        model.addAttribute("enrollmentCount", statisticService.countAllEnrollment());
        model.addAttribute("studentStatisticByCourse", statisticService.studentStatisticByCourse());
        model.addAttribute("top5CourseBestSeller", statisticService.top5CourseBestSeller());

        return "admin/dashboard";
    }
}
