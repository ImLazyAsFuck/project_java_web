package com.jarproject.controller;

import com.jarproject.entity.Course;
import com.jarproject.entity.Enrollment;
import com.jarproject.entity.EnrollmentStatus;
import com.jarproject.entity.Student;
import com.jarproject.service.course.CourseService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("course")
public class CourseController{
    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    HttpSession session;
    @Autowired
    private StudentService studentService;

    @GetMapping("list")
    public String courseList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String name,
            Model model
    ){
        Integer curUser = (Integer)session.getAttribute("curUser");
        Student sessionStudent = null;

        if (curUser != null) {
            sessionStudent = studentService.findById(curUser);
            if (sessionStudent != null && sessionStudent.isStatus()) {
                model.addAttribute("isLogin", true);
            } else {
                sessionStudent = null;
                model.addAttribute("isLogin", false);
            }
        } else {
            model.addAttribute("isLogin", false);
        }


        getData(page, name, model, sessionStudent);
        model.addAttribute("isRegistry", false);

        return "main-page/course-list";
    }


    private void getData(int page, String name, Model model, Student sessionStudent){
        int size = 5;
        List<Course> courses;
        long totalItems;
        long totalPages;

        List<Enrollment> enrollments = sessionStudent != null
                ? enrollmentService.getEnrollmentsByStudentId(sessionStudent.getId())
                : new ArrayList<>();

        Map<String, String> enrollmentStatusMap = enrollments.stream()
                .collect(Collectors.toMap(
                        e -> e.getCourse().getId(),
                        e -> e.getStatus().name()
                ));

        if(name == null || name.trim().isEmpty()){
            courses = courseService.findAllCourse(page, size);
            totalItems = courseService.count(true);
        }else{
            courses = courseService.searchByCourseName(name, page, size);
            totalItems = courseService.countByName(name, true);
        }

        totalPages = totalItems > 0 ? (long)Math.ceil((double)totalItems / size) : 0;

        model.addAttribute("courses", courses);
        model.addAttribute("enrollmentStatusMap", enrollmentStatusMap);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", name);
    }


    @GetMapping("register")
    public String courseRegister(
            String courseId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String name,
            Model model
    ){
        Integer curUser = (Integer)session.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }

        Student sessionStudent = studentService.findById(curUser);
        if(sessionStudent == null || !sessionStudent.isStatus()){
            return "redirect:/login";
        }

        if(courseId == null || courseId.trim().isEmpty()){
            return "redirect:/course/list";
        }

        Course course = courseService.findById(courseId);
        if(course == null || !course.isStatus()){
            return "redirect:/course/list";
        }

        Enrollment enrollment = enrollmentService.getEnrollmentByIdAndStudentId(course.getId(), sessionStudent.getId());
        if(enrollment != null){
            switch(enrollment.getStatus()){
                case CONFIRMED:
                case WAITING:
                    return "redirect:/course/list";
                default:
                    break;
            }
        }

        model.addAttribute("course", course);
        getData(page, name, model, sessionStudent);
        model.addAttribute("isRegistry", true);
        return "main-page/course-list";
    }


    @PostMapping("register")
    public String postRegister(@RequestParam("courseId") String courseId){
        Integer curUser = (Integer)session.getAttribute("curUser");
        if(curUser == null){
            return "redirect:/login";
        }

        Student student = studentService.findById(curUser);
        if(student == null || !student.isStatus()){
            return "redirect:/login";
        }

        Course course = courseService.findById(courseId);
        if(course == null || !course.isStatus()){
            return "redirect:/course/list";
        }

        Enrollment existing = enrollmentService.getEnrollmentByIdAndStudentId(courseId, student.getId());
        if(existing != null){
            if(existing.getStatus() == EnrollmentStatus.CONFIRMED || existing.getStatus() == EnrollmentStatus.WAITING){
                return "redirect:/course/list";
            }

            existing.setStatus(EnrollmentStatus.WAITING);
            enrollmentService.updateEnrollment(existing);
        }else{
            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(course);
            enrollment.setStudent(student);

            enrollmentService.addEnrollment(enrollment);
        }

        return "redirect:/course/list";
    }
}