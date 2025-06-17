package com.jarproject.service.statistic;

import com.jarproject.dto.CourseEnrollmentStatistic;

import java.util.List;

public interface StatisticService{
    long countAllCourse();
    long countAllStudent();
    long countAllEnrollment();
    List<CourseEnrollmentStatistic> studentStatisticByCourse();
    List<CourseEnrollmentStatistic> top5CourseBestSeller();
}
