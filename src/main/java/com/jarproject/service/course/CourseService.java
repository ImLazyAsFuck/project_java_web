package com.jarproject.service.course;

import com.jarproject.entity.Course;

import java.util.List;

public interface CourseService{
    List<Course> findAll();
    List<Course> findAllCourse(int page, int size);
    Course findById(String id);
    Course findByName(String name);
    void save(Course course);
    void delete(String id);
    void update(Course course);
    List<Course> searchByName(String name, int page, int size, String orderBy, String orderType, boolean status);
    List<Course> searchByCourseName(String name, int page, int size);
    List<Course> findAll(int page, int size, String orderBy, String orderType, boolean status);
    long count(boolean status);
    long countByName(String name, boolean status);
}
