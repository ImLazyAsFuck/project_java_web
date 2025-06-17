package com.jarproject.repository.statistic;

import com.jarproject.dto.CourseEnrollmentStatistic;
import com.jarproject.entity.Enrollment;

import java.util.List;

public interface StatisticRepository{
    long countAllCourse();
    long countAllStudent();
    long countAllEnrollment();
    List<CourseEnrollmentStatistic> studentStatisticByCourse();
    List<CourseEnrollmentStatistic> top5CourseBestSeller();
}
