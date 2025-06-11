package com.jarproject.controller;

import com.jarproject.dto.LoginDto;
import com.jarproject.dto.StudentDto;
import com.jarproject.entity.Student;
import com.jarproject.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController{
    @Autowired
    private StudentService studentService;

    @GetMapping("login")
    public String login(Model model){
        model.addAttribute("loginDto", new LoginDto());
        return "auth/login";
    }

    @GetMapping("register")
    public String register(Model model){
        model.addAttribute("studentDto", new StudentDto());
        return "auth/register";
    }

    @PostMapping("login")
    public String login(@Valid @ModelAttribute LoginDto loginDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "auth/login";
        }
        Student loginStudent = studentService.login(loginDto.getUsername(), loginDto.getPassword());
        if(loginStudent != null && loginStudent.isRole()){
            return "redirect:/admin";
        }else if(loginStudent != null){
            return "redirect:/home";
        }else{
            model.addAttribute("loginDto", loginDto);
            bindingResult.rejectValue("username", "error.username", "Username or password is incorrect");
            bindingResult.rejectValue("password", "error.password", "Username or password is incorrect");
            return "auth/login";
        }
    }

    @PostMapping("register")
    public String register(@Valid @ModelAttribute StudentDto studentDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "auth/register";
        }
        if(studentService.isUsernameExist(studentDto.getUsername())){
            bindingResult.rejectValue("username", "error.username", "Username already exist");
            return "auth/register";
        }
        if(studentService.isEmailExist(studentDto.getEmail())){
            bindingResult.rejectValue("email", "error.email", "Email already exist");
            return "auth/register";
        }
        if(studentService.isPhoneExist(studentDto.getPhone())){
            bindingResult.rejectValue("phone", "error.phone", "Phone already exist");
            return "auth/register";
        }
        Student student = new Student();
        student.setUsername(studentDto.getUsername());
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setPhone(studentDto.getPhone());
        student.setPassword(studentDto.getPassword());
        student.setDob(studentDto.getDob());
        student.setGender(studentDto.isGender());
        studentService.save(student);
        return "redirect:/login";
    }
}
