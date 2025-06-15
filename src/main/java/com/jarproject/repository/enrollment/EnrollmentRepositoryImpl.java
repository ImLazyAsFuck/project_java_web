package com.jarproject.repository.enrollment;

import com.jarproject.entity.Enrollment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Enrollment> getEnrollments() {
        try{
            return em.createQuery("SELECT e FROM Enrollment e", Enrollment.class)
                    .getResultList();
        }catch(Exception e){
            return Collections.emptyList();
        }
    }

    @Override
    public void addEnrollment(Enrollment e) {
        try{
            em.persist(e);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void updateEnrollment(Enrollment e) {
        try{
            em.merge(e);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(Integer studentId) {
        try{
            return em.createQuery(
                            "SELECT e FROM Enrollment e WHERE e.student.id = :studentId", Enrollment.class)
                    .setParameter("studentId", studentId)
                    .getResultList();
        }catch(Exception e){
            return Collections.emptyList();
        }
    }

    @Override
    public Enrollment getEnrollmentByIdAndStudentId(String courseId, Integer studentId){
        try{
            return em.createQuery(
                    "SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId", Enrollment.class)
                    .setParameter("studentId", studentId)
                    .setParameter("courseId", courseId)
                    .getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
}
