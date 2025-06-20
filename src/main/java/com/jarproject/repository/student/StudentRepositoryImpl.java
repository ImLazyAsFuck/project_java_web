package com.jarproject.repository.student;

import com.jarproject.config.PasswordUtil;
import com.jarproject.entity.Student;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class StudentRepositoryImpl implements StudentRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Student> findAll(){
        try{
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all students", e);
        }
    }

    @Override
    public Student findById(int id){
        try {
            return em.find(Student.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void save(Student student){
        try {
            student.setPassword(PasswordUtil.hashPassword(student.getPassword()));
            em.persist(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student student){
        try {
            em.merge(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id){
        try {
            Student student = em.find(Student.class, id);
            if (student != null) {
                em.remove(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> findAll(int page, int size){
        try {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isUsernameExist(String username){
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(s) FROM Student s WHERE s.username = :username", Long.class);
            query.setParameter("username", username);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isEmailExist(String email){
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(s) FROM Student s WHERE s.email = :email", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPhoneExist(String phone){
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(s) FROM Student s WHERE s.phone = :phone", Long.class);
            query.setParameter("phone", phone);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Student login(String email, String password) {
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.email = :email", Student.class
            );
            query.setParameter("email", email);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isEmailExistExceptId(String email, Integer excludeId) {
        try{
            TypedQuery<Boolean> query = em.createQuery(
                    "SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Student s WHERE s.email = :email AND s.id <> :excludeId", Boolean.class
            );
            query.setParameter("email", email);
            query.setParameter("excludeId", excludeId);
            return query.getSingleResult();
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean isPhoneExistExceptId(String phone, Integer excludeId){
        try{
            TypedQuery<Boolean> query = em.createQuery(
                    "SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END FROM Student s WHERE s.phone = :phone AND s.id <> :excludeId", Boolean.class
            );
            query.setParameter("phone", phone);
            query.setParameter("excludeId", excludeId);
            return query.getSingleResult();
        }catch(Exception e){
            return false;
        }
    }


    @Override
    public List<Student> searchByNameAndEmail(String kw, int page, int size, String orderBy, String orderType) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT s FROM Student s WHERE s.role = false AND " +
                    "(LOWER(s.name) LIKE LOWER(:kw) OR LOWER(s.email) LIKE LOWER(:kw))");
            if (orderBy != null && !orderBy.trim().isEmpty()) {
                if ("name".equals(orderBy) || "email".equals(orderBy)) {
                    jpql.append(" ORDER BY s.").append(orderBy);
                    if ("desc".equals(orderType)) {
                        jpql.append(" DESC");
                    } else {
                        jpql.append(" ASC");
                    }
                }
            }
            TypedQuery<Student> query = em.createQuery(jpql.toString(), Student.class);
            query.setParameter("kw", "%" + kw + "%");
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    @Override
    public List<Student> findAll(int page, int size, String orderBy, String orderType){
        try{
            StringBuilder jpql = new StringBuilder("SELECT s FROM Student s WHERE s.role = false");

            if (orderBy != null && !orderBy.trim().isEmpty()) {
                if ("name".equals(orderBy) || "email".equals(orderBy)) {
                    jpql.append(" ORDER BY s.").append(orderBy);
                    if ("desc".equals(orderType)) {
                        jpql.append(" DESC");
                    } else {
                        jpql.append(" ASC");
                    }
                }
            }

            TypedQuery<Student> query = em.createQuery(jpql.toString(), Student.class);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            return query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    @Override
    public long count(){
        try{
            TypedQuery<Long> query = em.createQuery("select count(s) from Student s where s.role = false", Long.class);
            return query.getSingleResult();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public long countByNameAndEmail(String kw) {
        try {
            String jpql = "SELECT COUNT(s) FROM Student s WHERE s.role = false AND " +
                    "(LOWER(s.name) LIKE LOWER(:kw) OR LOWER(s.email) LIKE LOWER(:kw))";

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("kw", "%" + kw + "%");

            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
