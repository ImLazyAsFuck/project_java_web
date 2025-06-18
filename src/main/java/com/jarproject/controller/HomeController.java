package com.jarproject.controller;

import com.jarproject.config.PasswordUtil;
import com.jarproject.dto.StudentDto;
import com.jarproject.entity.Student;
import com.jarproject.service.course.CourseService;
import com.jarproject.service.student.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("home")
public class HomeController{
    @Autowired
    private CourseService courseService;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public String home(){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus()){
            return "redirect:/login";
        }
        return "redirect:/course/list";
    }

    @GetMapping("enrollment")
    public String enrollment(){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus()){
            return "redirect:/login";
        }
        return "enrollment";
    }

}
