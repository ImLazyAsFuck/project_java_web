package com.jarproject.dto;

import com.jarproject.entity.Course;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentStatistic {
    private Course course;
    private Long studentCount;
}
