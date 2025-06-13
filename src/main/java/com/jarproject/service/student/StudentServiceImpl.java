package com.jarproject.service.student;

import com.jarproject.entity.Student;
import com.jarproject.repository.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    @Override
    public Student findById(int id){
        return studentRepository.findById(id);
    }

    @Override
    public void save(Student student){
        studentRepository.save(student);
    }

    @Override
    public void update(Student student){
        studentRepository.save(student);
    }

    @Override
    public void delete(Integer id){
        studentRepository.delete(id);
    }

    @Override
    public List<Student> findAll(int page, int size){
        return studentRepository.findAll(page, size);
    }

    @Override
    public boolean isUsernameExist(String username){
        return studentRepository.isUsernameExist(username);
    }

    @Override
    public boolean isEmailExist(String email){
        return studentRepository.isEmailExist(email);
    }

    @Override
    public boolean isPhoneExist(String phone){
        return studentRepository.isPhoneExist(phone);
    }

    @Override
    public Student login(String email, String password){
        return studentRepository.login(email, password);
    }
}
