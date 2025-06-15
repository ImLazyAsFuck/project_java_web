package com.jarproject.controller;

import com.jarproject.entity.Student;
import com.jarproject.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping
    public String admin(){
        return "redirect:/admin/dashboard";
    }

    @GetMapping("dashboard")
    public String dashboard(){
        Integer curUser = (Integer) session.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
            return "redirect:/login";
        }
        return "admin/dashboard";
    }
}
