package com.jarproject.controller;

import com.jarproject.config.PasswordUtil;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthController{
    @Autowired
    private StudentService studentService;
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/")
    public String index() {
        Integer curUser = (Integer) httpSession.getAttribute("curUser");

        if (curUser == null) {
            return "redirect:/course/list";
        }

        Student student = studentService.findById(curUser);

        if (student == null || !student.isStatus()) {
            return "redirect:/course/list";
        }

        return student.isRole() ? "redirect:/admin" : "redirect:/course/list";
    }


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
    public String login(@Valid @ModelAttribute LoginDto loginDto, BindingResult bindingResult, Model model,
    RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "auth/login";
        }

        Student loginStudent = studentService.login(loginDto.getEmail(), loginDto.getPassword());

        if(loginStudent != null && PasswordUtil.verifyPassword(loginDto.getPassword(), loginStudent.getPassword())) {
            if(loginStudent.isRole()) {
                httpSession.setAttribute("curUser", loginStudent.getId());
                return "redirect:/admin";
            } else {
                httpSession.setAttribute("curUser", loginStudent.getId());
                redirectAttributes.addFlashAttribute("successMessage", "Login successfully");
                return "redirect:/course/list";
            }
        } else {
            model.addAttribute("loginDto", loginDto);
            bindingResult.rejectValue("email", "error.email", "Email or password is incorrect");
            bindingResult.rejectValue("password", "error.password", "Email or password is incorrect");
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

    @GetMapping("logout")
    public String logout(){
        httpSession.removeAttribute("curUser");
        return "redirect:/login";
    }
}
