package com.jarproject.controller;

import com.jarproject.entity.Enrollment;
import com.jarproject.entity.EnrollmentStatus;
import com.jarproject.entity.Student;
import com.jarproject.service.enrollment.EnrollmentService;
import com.jarproject.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("enrollment")
public class EnrollmentController{
    @Autowired
    private StudentService studentService;
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private HttpSession httpSession;

    @GetMapping
    public String enrollments(){
        return "redirect:/enrollment/list";
    }

    @GetMapping("list")
    public String enrollmentList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String kw,
            Model model
    ){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus() || sessionStudent.isRole()){
            return "redirect:/login";
        }

        getData(page, sort, kw, model, sessionStudent);
        model.addAttribute("isShowForm", false);
        model.addAttribute("isLogin", true);
        return "main-page/enrollment-list";
    }

    private void getData(int page, String sort, String kw, Model model, Student sessionStudent){
        int size = 5;
        List<Enrollment> enrollments;
        long totalItems;

        String orderType = "asc";

        if(sort != null && !sort.trim().isEmpty()){
            if(sort.equals("asc") || sort.equals("desc")){
                orderType = sort;
            }
        }

        if(kw != null && !kw.trim().isEmpty()){
            enrollments = enrollmentService.searchByCourseName(
                    kw.trim(), sessionStudent.getId(), page - 1, size, "status", orderType);
            totalItems = enrollmentService.countByCourseName(
                    kw.trim(), sessionStudent.getId());
        } else {
            enrollments = enrollmentService.getEnrollmentsByStudentId(
                    sessionStudent.getId(), page - 1, size, "status", orderType);
            totalItems = enrollmentService.count(sessionStudent.getId());
        }

        long totalPages = totalItems > 0 ? (long)Math.ceil((double)totalItems / size) : 0;

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("sort", sort);
        model.addAttribute("kw", kw);
    }

    @PostMapping("list")
    public String changeStatus(
            @RequestParam("id") Integer id,
            @RequestParam("status") String status,
            Model model){
        Integer curUser = (Integer) httpSession.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }
        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus() || sessionStudent.isRole()){
            return "redirect:/login";
        }

        if(id == null){
            return "redirect:/enrollment/list";
        }

        if(status == null || status.trim().isEmpty()){
            return "redirect:/enrollment/list";
        }

        Enrollment e = enrollmentService.findById(id);

        if(e == null){
            return "redirect:/enrollment/list";
        }

        if(!e.getStatus().equals(EnrollmentStatus.WAITING)){
            return "redirect:/enrollment/list";
        }
        try {
            e.setStatus(EnrollmentStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return "redirect:/enrollment/list";
        }
        enrollmentService.updateEnrollment(e);
        model.addAttribute("isLogin", true);

        return "redirect:/enrollment/list";
    }
}
