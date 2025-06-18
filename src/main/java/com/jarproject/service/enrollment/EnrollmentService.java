package com.jarproject.service.enrollment;

import com.jarproject.entity.Enrollment;

import java.util.List;

public interface EnrollmentService{
    List<Enrollment> getEnrollments();
    void addEnrollment(Enrollment e);
    void updateEnrollment(Enrollment e);
    List<Enrollment> getEnrollmentsByStudentId(Integer studentId);
    Enrollment getEnrollmentByIdAndStudentId(String courseId, Integer studentId);
    Enrollment findById(Integer id);

    List<Enrollment> getEnrollmentsByStudentId(Integer studentId, int page, int size, String orderBy, String orderType);
    long count(Integer studentId);
    List<Enrollment> searchByCourseName(String name, Integer studentId, int page, int size, String orderBy, String orderType);
    long countByCourseName(String name, Integer studentId);

    List<Enrollment> findAll(int page, int size, String status);
    List<Enrollment> searchByCourseName(String name, int page, int size, String status);
    long countByCourseName(String name, String status);
    long countEnrollment(String status);
}
