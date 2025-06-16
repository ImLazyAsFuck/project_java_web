package com.jarproject.service.course;

import com.jarproject.entity.Course;
import com.jarproject.repository.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> findAll(){
        return courseRepository.findAll()   ;
    }

    @Override
    public List<Course> findAllCourse(int page, int size){
        return courseRepository.findAllCourse(page, size);
    }

    @Override
    public Course findById(String id){
        return courseRepository.findById(id);
    }

    @Override
    public Course findByName(String name){
        return courseRepository.findByName(name);
    }

    @Override
    public void save(Course course){
        courseRepository.save(course);
    }

    @Override
    public void delete(String id){
        courseRepository.delete(id);
    }

    @Override
    public void update(Course course){
        courseRepository.save(course);
    }

    @Override
    public List<Course> searchByName(String name, int page, int size, String orderBy, String orderType, boolean status){
        return courseRepository.searchByName(name,page,size,orderBy,orderType, status);
    }

    @Override
    public List<Course> searchByCourseName(String name, int page, int size){
        return courseRepository.searchByCourseName(name,page,size);
    }


    @Override
    public List<Course> findAll(int page, int size, String orderBy, String orderType, boolean status) {
        return courseRepository.findAll(page, size, orderBy, orderType, status);
    }

    @Override
    public long count(boolean status){
        return courseRepository.count(status);
    }

    @Override
    public long countByName(String name, boolean status){
        return courseRepository.countByName(name,status);
    }


}
