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

    @Override
    public Enrollment findById(Integer id){
        return enrollmentRepository.findById(id);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(Integer studentId, int page, int size, String orderBy, String orderType){
        return enrollmentRepository.getEnrollmentsByStudentId(studentId, page, size, orderBy, orderType);
    }

    @Override
    public long count(Integer studentId){
        return enrollmentRepository.count(studentId);
    }

    @Override
    public List<Enrollment> searchByCourseName(String name, Integer studentId, int page, int size, String orderBy, String orderType){
        return enrollmentRepository.searchByCourseName(name, studentId, page, size, orderBy, orderType);
    }

    @Override
    public long countByCourseName(String name, Integer studentId){
        return enrollmentRepository.countByCourseName(name, studentId);
    }

    @Override
    public List<Enrollment> findAll(int page, int size, String status){
        return enrollmentRepository.findAll(page, size, status);
    }

    @Override
    public List<Enrollment> searchByCourseName(String name, int page, int size, String status){
        return enrollmentRepository.searchByCourseName(name, page, size, status);
    }

    @Override
    public long countByCourseName(String name, String status){
        return enrollmentRepository.countByCourseName(name, status);
    }

    @Override
    public long countEnrollment(String status){
        return enrollmentRepository.countEnrollment(status);
    }
}
