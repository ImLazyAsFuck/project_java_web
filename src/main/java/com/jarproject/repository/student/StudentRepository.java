package com.jarproject.repository.student;

import com.jarproject.entity.Student;

import java.util.List;

public interface StudentRepository{
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

}
