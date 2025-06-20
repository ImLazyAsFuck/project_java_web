package com.jarproject.controller;

import com.jarproject.entity.Student;
import com.jarproject.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ErrorController {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private StudentService studentService;

    @GetMapping("not-found")
    public String notFoundPage(){
        return "error/404";
    }

    @GetMapping("access-denied")
    public String accessDeniedPage(){
        return "error/403";
    }

    @GetMapping("unauthorized")
    public String unauthorizedPage(){
        return "error/401";
    }

    @GetMapping("internal-error")
    public String internalServerErrorPage() {
        return "error/500";
    }

    @GetMapping("method-not-allowed")
    public String methodNotAllowedPage() {
        return "error/405";
    }
}