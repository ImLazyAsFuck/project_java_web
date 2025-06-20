package com.jarproject.controller;

import com.jarproject.entity.Enrollment;
import com.jarproject.entity.EnrollmentStatus;
import com.jarproject.entity.Student;
import com.jarproject.service.enrollment.EnrollmentService;
import com.jarproject.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/enrollment")
public class AdminEnrollmentController{
    private final StudentService studentService;
    private final javax.servlet.http.HttpSession session;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public String adminEnrollment(){
        return "redirect:/admin/enrollment/list";
    }

    @GetMapping("list")
    public String list(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String kw
    ){
        Integer curUser = (Integer) session.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
            return "redirect:/login";
        }

        getData(model, page, status, kw);

        return "/admin/enrollment-management/enrollment-list";
    }

    private void getData(Model model, int page, String status, String kw){
        int size = 10;
        List<Enrollment> enrollments;
        long totalItems;
        long totalPages;

        if(kw != null && !kw.trim().isEmpty()){
            enrollments = enrollmentService.searchByCourseName(kw, page, size, status);
            totalItems = enrollmentService.countByCourseName(kw, status);
        }else{
            enrollments = enrollmentService.findAll(page, size, status);
            totalItems = enrollmentService.countEnrollment(status);
        }

        totalPages = totalItems > 0 ? (long)Math.ceil((double)totalItems / size) : 1;
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("status", status);
        model.addAttribute("kw", kw);
    }

    @PostMapping("list")
    public String changeStatus(
            @RequestParam("id") Integer id,
            @RequestParam("status") String status
    ){
        Integer curUser = (Integer) session.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus() || !sessionStudent.isRole()){
            return "redirect:/login";
        }

        if(id == null){
            return "redirect:/admin/enrollment/list";
        }

        if(status == null || status.trim().isEmpty()){
            return "redirect:/admin/enrollment/list";
        }

        Enrollment e = enrollmentService.findById(id);

        if(e == null){
            return "redirect:/admin/enrollment/list";
        }
        if(!e.getStatus().equals(EnrollmentStatus.WAITING)){
            return "redirect:/admin/enrollment/list";
        }

        e.setStatus(EnrollmentStatus.valueOf(status.toUpperCase()));
        enrollmentService.updateEnrollment(e);

        return "redirect:/admin/enrollment/list";
    }
}
