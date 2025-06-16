package com.jarproject.repository.enrollment;

import com.jarproject.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository{
    List<Enrollment> getEnrollments();
    void addEnrollment(Enrollment e);
    void updateEnrollment(Enrollment e);
    List<Enrollment> getEnrollmentsByStudentId(Integer studentId);
    Enrollment getEnrollmentByIdAndStudentId(String courseId, Integer studentId);

    List<Enrollment> getEnrollmentsByStudentId(Integer studentId, int page, int size, String orderBy, String orderType);
    long count(Integer studentId);
    List<Enrollment> searchByCourseName(String name, Integer studentId, int page, int size, String orderBy, String orderType);
    long countByCourseName(String name, Integer studentId);
}
