package com.jarproject.service.statistic;

import com.jarproject.dto.CourseEnrollmentStatistic;
import com.jarproject.repository.statistic.StatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService{
    @Autowired
    private StatisticRepository staRepository;

    @Override
    public long countAllCourse(){
        return staRepository.countAllCourse();
    }

    @Override
    public long countAllStudent(){
        return staRepository.countAllStudent();
    }

    @Override
    public long countAllEnrollment(){
        return staRepository.countAllEnrollment();
    }

    @Override
    public List<CourseEnrollmentStatistic> studentStatisticByCourse(){
        return staRepository.studentStatisticByCourse();
    }

    @Override
    public List<CourseEnrollmentStatistic> top5CourseBestSeller(){
        return staRepository.top5CourseBestSeller();
    }
}
