package com.jarproject.controller;

import com.jarproject.entity.Student;
import com.jarproject.service.Course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("home")
public class HomeController{
    @Autowired
    private CourseService courseService;
    @Autowired
    private HttpSession httpSession;

    @GetMapping
    public String home(){
        Student student = (Student) httpSession.getAttribute("loginStudent");
        if(student == null){
            return "redirect:/login";
        }
        return "redirect:/course/list";
    }

    @GetMapping("enrollment")
    public String enrollment(){
        Student student = (Student) httpSession.getAttribute("loginStudent");
        if(student == null){
            return "redirect:/login";
        }
        return "enrollment";
    }

    @GetMapping("profile")
    public String profile(){
        Student student = (Student) httpSession.getAttribute("loginStudent");
        if(student == null){
            return "redirect:/login";
        }
        return "profile";
    }
}
