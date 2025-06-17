package com.jarproject.repository.statistic;

import com.jarproject.dto.CourseEnrollmentStatistic;
import com.jarproject.entity.Course;
import com.jarproject.entity.Enrollment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatisticRepositoryImpl implements StatisticRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public long countAllCourse(){
        try{
            return em.createQuery("SELECT COUNT(c) FROM Course c where c.status=true", Long.class)
                    .getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public long countAllStudent(){
        try{
            return em.createQuery("SELECT COUNT(s) FROM Student s where s.status=true and s.role = false", Long.class)
                    .getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public long countAllEnrollment(){
        try{
            return em.createQuery("SELECT COUNT(e) FROM Enrollment e", Long.class)
                    .getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public List<CourseEnrollmentStatistic> studentStatisticByCourse() {
        try {
            List<Object[]> results = em.createQuery(
                    "SELECT c, " +
                            "(SELECT COUNT(e2) FROM Enrollment e2 " +
                            " WHERE e2.course = c AND e2.student.status = true and e2.student.role = false) as studentCount " +
                            "FROM Course c " +
                            "WHERE c.status = true " +
                            "ORDER BY studentCount DESC",
                    Object[].class
            ).getResultList();

            List<CourseEnrollmentStatistic> stats = new ArrayList<>();
            for (Object[] row : results) {
                Course course = (Course) row[0];
                Long count = (Long) row[1];
                stats.add(new CourseEnrollmentStatistic(course, count));
            }
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<CourseEnrollmentStatistic> top5CourseBestSeller() {
        try {
            List<Object[]> results = em.createQuery(
                            "SELECT c, " +
                                    "(SELECT COUNT(e2) FROM Enrollment e2 " +
                                    " WHERE e2.course = c AND e2.student.role = false) as enrollmentCount " +
                                    "FROM Course c " +
                                    "WHERE c.status = true " +
                                    "ORDER BY enrollmentCount DESC",
                            Object[].class
                    )
                    .setMaxResults(5)
                    .getResultList();

            List<CourseEnrollmentStatistic> stats = new ArrayList<>();
            for (Object[] row : results) {
                Course course = (Course) row[0];
                Long count = (Long) row[1];
                stats.add(new CourseEnrollmentStatistic(course, count));
            }
            return stats;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
