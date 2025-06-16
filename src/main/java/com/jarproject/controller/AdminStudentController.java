package com.jarproject.controller;

import com.jarproject.entity.Student;
import com.jarproject.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("admin/student")
public class AdminStudentController{
    @Autowired
    private StudentService studentService;
    @Autowired
    private HttpSession session;

    @GetMapping
    public String adminStudent(){
        return "redirect:/admin/student/list";
    }

    @GetMapping("list")
    public String showStudent(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String sort,
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

        int size = 10;

        String orderBy = "name";
        String orderType = "asc";

        if (sort != null && !sort.trim().isEmpty()) {
            String[] sortParts = sort.split("_");
            if (sortParts.length == 2) {
                String field = sortParts[0];
                String direction = sortParts[1];

                if ("name".equals(field) || "email".equals(field)) {
                    orderBy = field;
                }

                if ("asc".equals(direction) || "desc".equals(direction)) {
                    orderType = direction;
                }
            }
        }
        List<Student> students;
        long totalItems;
        long totalPages;
        if(kw != null && !kw.trim().isEmpty()){
            students = studentService.searchByNameAndEmail(kw, page, size, orderBy, orderType);
            totalItems = studentService.countByNameAndEmail(kw);
        }else{
            students = studentService.findAll(page, size, orderBy, orderType);
            totalItems = studentService.count();
        }
        totalPages = (long) Math.ceil((double) totalItems / size);
        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("kw", kw);
        return "admin/student-management/student-list";
    }
}
