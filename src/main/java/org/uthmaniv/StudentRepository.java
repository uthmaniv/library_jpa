package org.uthmaniv;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class StudentRepository {
    private final EntityManager em;

    public StudentRepository(EntityManager em) {
        this.em = em;
    }

    public Student save(Student s) {
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
        return s;
    }

    public Student findById(String id) {
        return em.find(Student.class, id);
    }

    public Student findByName(String firstName, String lastName) {
        try {
            return em.createQuery(
                            "SELECT s FROM Student s WHERE s.firstName = :firstName AND s.lastName = :lastName",
                            Student.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}

