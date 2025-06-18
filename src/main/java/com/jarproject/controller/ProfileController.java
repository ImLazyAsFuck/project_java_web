package com.jarproject.controller;

import com.jarproject.config.PasswordUtil;
import com.jarproject.dto.PasswordDto;
import com.jarproject.dto.StudentDto;
import com.jarproject.entity.Student;
import com.jarproject.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController{
    private final HttpSession httpSession;
    private final StudentService studentService;
    private final ModelMapper modelMapper;

    @GetMapping
    public String profile(Model model){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus()){
            return "redirect:/login";
        }
        model.addAttribute("isShowForm", false);
        getData(model, sessionStudent);
        return "main-page/profile";
    }

    private void getData(Model model, Student sessionStudent){
        StudentDto studentDto = modelMapper.map(sessionStudent, StudentDto.class);
        model.addAttribute("studentDto", studentDto);
    }

    @PostMapping("change-password")
    public String profileEdit(
            @Valid @ModelAttribute PasswordDto passwordDto,
            BindingResult bindingResult,
            Model model
    ){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus()){
            return "redirect:/login";
        }

        getData(model, sessionStudent);
        model.addAttribute("isShowForm", true);
        model.addAttribute("passwordDto", passwordDto);

        if(bindingResult.hasErrors()){
            return "main-page/profile";
        }

        if(!PasswordUtil.verifyPassword(passwordDto.getOldPassword(), sessionStudent.getPassword())){
            bindingResult.rejectValue("oldPassword", "error.oldPassword", "Old password is incorrect");
            return "main-page/profile";
        }
        if(!Objects.equals(passwordDto.getNewPassword(), passwordDto.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Confirm password is incorrect");
            return "main-page/profile";
        }
        if(PasswordUtil.verifyPassword(passwordDto.getNewPassword(), sessionStudent.getPassword())){
            bindingResult.rejectValue("newPassword", "error.newPassword", "New password cannot be same as old password");
            return "main-page/profile";
        }
        sessionStudent.setPassword(PasswordUtil.hashPassword(passwordDto.getNewPassword()));
        studentService.update(sessionStudent);
        model.addAttribute("isShowForm", false);
        return "redirect:/profile";
    }


    @GetMapping("change-password")
    public String profileEdit(Model model){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus()){
            return "redirect:/login";
        }

        getData(model, sessionStudent);
        model.addAttribute("isShowForm", true);
        model.addAttribute("passwordDto", new PasswordDto());
        return "main-page/profile";
    }

    @PostMapping
    public String profileEdit(
            @Valid @ModelAttribute StudentDto studentDto,
            BindingResult bindingResult,
            Model model
            ){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus()){
            return "redirect:/login";
        }
        getData(model, sessionStudent);
        if(bindingResult.hasErrors()){
            return "main-page/profile";
        }

        if (studentService.isEmailExistExceptId(studentDto.getEmail(), curUser)) {
            bindingResult.rejectValue("email", "email.duplicate", "Email này đã được sử dụng bởi tài khoản khác");
            return "main-page/profile";
        }

        modelMapper.map(studentDto, sessionStudent);
        studentService.update(sessionStudent);
        return "redirect:/profile";
    }
}
