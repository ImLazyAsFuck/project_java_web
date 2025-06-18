package com.jarproject.repository.enrollment;

import com.jarproject.entity.Enrollment;
import com.jarproject.entity.EnrollmentStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    @Override
    public Enrollment findById(Integer id){
        try{
            TypedQuery<Enrollment> query = em.createQuery(
                    "SELECT e FROM Enrollment e WHERE e.id = :id", Enrollment.class)
                    .setParameter("id", id);
            return query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(Integer studentId, int page, int size, String orderBy, String orderType){
        try{
            String jpql = "SELECT e FROM Enrollment e WHERE e.student.id = :studentId";

            if (orderBy != null && !orderBy.trim().isEmpty()) {
                if ("status".equalsIgnoreCase(orderBy)) {
                    jpql += " ORDER BY e.status";
                } else {
                    jpql += " ORDER BY e.id";
                }

                if ("desc".equalsIgnoreCase(orderType)) {
                    jpql += " DESC";
                } else {
                    jpql += " ASC";
                }
            }

            TypedQuery<Enrollment> query = em.createQuery(jpql, Enrollment.class)
                    .setParameter("studentId", studentId)
                    .setFirstResult(page * size)
                    .setMaxResults(size);

            return query.getResultList();
        }catch(Exception e){
            return Collections.emptyList();
        }
    }

    @Override
    public long count(Integer studentId){
        try{
            return em.createQuery(
                            "SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId", Long.class)
                    .setParameter("studentId", studentId)
                    .getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public List<Enrollment> searchByCourseName(String name, Integer studentId, int page, int size, String orderBy, String orderType){
        try{
            String jpql = "SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND LOWER(e.course.name) LIKE LOWER(:courseName)";


            if (orderBy != null && !orderBy.trim().isEmpty()) {
                if ("status".equalsIgnoreCase(orderBy)) {
                    jpql += " ORDER BY e.status";
                } else {
                    jpql += " ORDER BY e.id";
                }

                if ("desc".equalsIgnoreCase(orderType)) {
                    jpql += " DESC";
                } else {
                    jpql += " ASC";
                }
            }

            TypedQuery<Enrollment> query = em.createQuery(jpql, Enrollment.class)
                    .setParameter("studentId", studentId)
                    .setParameter("courseName", "%" + name + "%")
                    .setFirstResult(page * size)
                    .setMaxResults(size);

            return query.getResultList();
        }catch(Exception e){
            return Collections.emptyList();
        }
    }

    @Override
    public long countByCourseName(String name, Integer studentId){
        try{
            return em.createQuery(
                            "SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND LOWER(e.course.name) LIKE LOWER(:courseName)", Long.class)
                    .setParameter("studentId", studentId)
                    .setParameter("courseName", "%" + name + "%")
                    .getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public List<Enrollment> findAll(int page, int size, String status){
        try{
            StringBuilder jpqlBuilder = new StringBuilder("SELECT e FROM Enrollment e");

            if (status != null && !status.trim().isEmpty()) {
                jpqlBuilder.append(" WHERE e.status = :status");
            }

            TypedQuery<Enrollment> query = em.createQuery(jpqlBuilder.toString(), Enrollment.class);

            if (status != null && !status.trim().isEmpty()) {
                query.setParameter("status", EnrollmentStatus.valueOf(status));
            }

            return query.setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        }catch(Exception e){
            return Collections.emptyList();
        }
    }

    @Override
    public List<Enrollment> searchByCourseName(String name, int page, int size, String status){
        try{
            String jpql = "SELECT e FROM Enrollment e WHERE LOWER(e.course.name) LIKE LOWER(:courseName)";

            if (status != null && !status.trim().isEmpty()) {
                jpql += " AND e.status = :status";
            }

            TypedQuery<Enrollment> query = em.createQuery(jpql, Enrollment.class)
                    .setParameter("courseName", "%" + name + "%");

            if (status != null && !status.trim().isEmpty()) {
                query.setParameter("status", EnrollmentStatus.valueOf(status));
            }

            query.setFirstResult((page - 1) * size)
                    .setMaxResults(size);

            return query.getResultList();
        }catch(Exception e){
            return Collections.emptyList();
        }
    }



    public long countByCourseName(String name, String status) {
        try {
            String jpql = "SELECT COUNT(e) FROM Enrollment e WHERE LOWER(e.course.name) LIKE LOWER(:courseName)";

            if (status != null && !status.trim().isEmpty()) {
                jpql += " AND e.status = :status";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                    .setParameter("courseName", "%" + name + "%");

            if (status != null && !status.trim().isEmpty()) {
                query.setParameter("status", status);
            }

            return query.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }

    public long countEnrollment(String status) {
        try {
            String jpql = "SELECT COUNT(e) FROM Enrollment e";

            if (status != null && !status.trim().isEmpty()) {
                jpql += " WHERE e.status = :status";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);

            if (status != null && !status.trim().isEmpty()) {
                query.setParameter("status", status);
            }

            return query.getSingleResult();
        } catch (Exception e) {
            return 0;
        }
    }


}
