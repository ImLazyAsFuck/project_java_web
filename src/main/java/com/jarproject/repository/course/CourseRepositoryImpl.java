package com.jarproject.repository.course;

import com.jarproject.entity.Course;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CourseRepositoryImpl implements CourseRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Course> findAll(){
        TypedQuery<Course> query = em.createQuery(
                "select c from Course c", Course.class);
        return query.getResultList();
    }

    @Override
    public Course findById(String id){
        try{
            return em.find(Course.class, id);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public Course findByName(String name){
        try{
            TypedQuery<Course> query = em.createQuery(
                    "select c from Course c where c.name = :name", Course.class
            );
            query.setParameter("name", name);
            return query.getSingleResult();
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public void save(Course course){
        try{
            if(course.getId() != null && !course.getId().isEmpty() && em.find(Course.class, course.getId()) != null) {
                em.merge(course);
            } else {
                em.persist(course);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void delete(String id){
        try{
            Course course = em.find(Course.class, id);
            if(course != null){
                course.setStatus(false);
                em.merge(course);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Course course){
        try{
            em.merge(course);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> searchByName(String name, int page, int size, String orderBy, String orderType, boolean status) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT c FROM Course c WHERE LOWER(c.name) LIKE :name AND c.status = :status");

            if (orderBy != null && (orderBy.equals("id") || orderBy.equals("name"))) {
                String direction = "desc".equalsIgnoreCase(orderType) ? "DESC" : "ASC";
                jpql.append(" ORDER BY c.").append(orderBy).append(" ").append(direction);
            }

            TypedQuery<Course> query = em.createQuery(jpql.toString(), Course.class);
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            query.setParameter("status", status);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }



    @Override
    public List<Course> findAll(int page, int size, String orderBy, String orderType, boolean status) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT c FROM Course c WHERE c.status = :status");

            if (orderBy != null && (orderBy.equals("id") || orderBy.equals("name"))) {
                String direction = "desc".equalsIgnoreCase(orderType) ? "DESC" : "ASC";
                jpql.append(" ORDER BY c.").append(orderBy).append(" ").append(direction);
            }

            TypedQuery<Course> query = em.createQuery(jpql.toString(), Course.class);
            query.setParameter("status", status);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    @Override
    public long count(boolean status) {
        try{
            TypedQuery<Long> query = em.createQuery("select count(c) from Course c where c.status=:status", Long.class);
            query.setParameter("status", status);
            return query.getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public long countByName(String name, boolean status) {
        try{
            TypedQuery<Long> query = em.createQuery("select count(c) from Course c where c.name=:name and c.status=:status", Long.class);
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            query.setParameter("status", status);
            return query.getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

}
