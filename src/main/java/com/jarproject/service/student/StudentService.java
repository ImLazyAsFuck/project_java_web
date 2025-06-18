package com.jarproject.service.student;

import com.jarproject.entity.Student;

import java.util.List;

public interface StudentService{
    List<Student> findAll();
    Student findById(int id);
    void save(Student student);
    void update(Student student);
    void delete(Integer id);
    List<Student> findAll(int page, int size);
    boolean isUsernameExist(String username);
    boolean isEmailExist(String email);
    boolean isPhoneExist(String phone);
    Student login(String email, String password);
    boolean isEmailExistExceptId(String email, Integer excludeId);

    List<Student> searchByNameAndEmail(String kw, int page, int size, String orderBy, String orderType);
    List<Student> findAll(int page, int size, String orderBy, String orderType);
    long count();
    long countByNameAndEmail(String name);
}
