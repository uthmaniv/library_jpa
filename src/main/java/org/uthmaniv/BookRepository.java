package org.uthmaniv;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class BookRepository {
    private final EntityManager em;
    private final JPAQueryFactory qf;

    public BookRepository(EntityManager em) {
        this.em = em;
        this.qf = new JPAQueryFactory(em);
    }

    public Book save(Book book) {
        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();
        return book;
    }

    public Book findById(String id) {
        return em.find(Book.class, id);
    }

    public Book findByTitle(String title) {
        return em.createQuery(
                        "SELECT b FROM Book b WHERE b.title = :title", Book.class)
                .setParameter("title", title)
                .getSingleResult();
    }


    // QueryDSL: get all books
    public List<Book> findAll() {
        QBook b = QBook.book;
        return qf.selectFrom(b).fetch();
    }

}

