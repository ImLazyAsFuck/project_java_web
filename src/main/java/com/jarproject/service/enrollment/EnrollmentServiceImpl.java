package com.jarproject.service.enrollment;

import com.jarproject.entity.Enrollment;
import com.jarproject.repository.enrollment.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public List<Enrollment> getEnrollments(){
        return enrollmentRepository.getEnrollments();
    }

    @Override
    public void addEnrollment(Enrollment e){
        enrollmentRepository.addEnrollment(e);
    }

    @Override
    public void updateEnrollment(Enrollment e){
        enrollmentRepository.updateEnrollment(e);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(Integer studentId){
        return enrollmentRepository.getEnrollmentsByStudentId(studentId);
    }

    @Override
    public Enrollment getEnrollmentByIdAndStudentId(String courseId, Integer studentId){
        return enrollmentRepository.getEnrollmentByIdAndStudentId(courseId, studentId);
    }
}
