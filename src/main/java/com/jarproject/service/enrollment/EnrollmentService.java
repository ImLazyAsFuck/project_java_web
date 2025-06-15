package com.jarproject.service.enrollment;

import com.jarproject.entity.Enrollment;

import java.util.List;

public interface EnrollmentService{
    List<Enrollment> getEnrollments();
    void addEnrollment(Enrollment e);
    void updateEnrollment(Enrollment e);
    List<Enrollment> getEnrollmentsByStudentId(Integer studentId);
    Enrollment getEnrollmentByIdAndStudentId(String courseId, Integer studentId);
}
