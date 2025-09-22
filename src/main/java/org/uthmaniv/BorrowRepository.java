package org.uthmaniv;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class BorrowRepository {
    private final EntityManager em;

    public BorrowRepository(EntityManager em) {
        this.em = em;
    }

    public BorrowRecord save(BorrowRecord br) {
        em.getTransaction().begin();
        em.persist(br);
        em.getTransaction().commit();
        return br;
    }

    public BorrowRecord update(BorrowRecord br) {
        em.getTransaction().begin();
        BorrowRecord merged = em.merge(br);
        em.getTransaction().commit();
        return merged;
    }

    // find active borrow for student (returnedAt is null)
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

