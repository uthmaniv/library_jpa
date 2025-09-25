package org.uthmaniv.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.uthmaniv.models.BorrowRecord;
import org.uthmaniv.models.Student;

import java.util.List;

public class BorrowRepository {
    private final EntityManager em;

    public BorrowRepository(EntityManager em) {
        this.em = em;
    }

    public void save(BorrowRecord br) {
        em.getTransaction().begin();
        em.persist(br);
        em.getTransaction().commit();
    }

    public void update(BorrowRecord br) {
        em.getTransaction().begin();
        BorrowRecord merged = em.merge(br);
        em.getTransaction().commit();
    }

    public BorrowRecord findActiveByStudent(Student student) {
        try {
            return em.createQuery("SELECT b FROM BorrowRecord b WHERE b.student = :s AND b.returnedAt IS NULL", BorrowRecord.class)
                    .setParameter("s", student)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<BorrowRecord> findAll() {
        return em.createQuery("SELECT b FROM BorrowRecord b", BorrowRecord.class).getResultList();
    }
}

