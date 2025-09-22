package org.uthmaniv;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.uthmaniv.exception.BookNotFoundException;

import java.util.List;

public class BookRepository {
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    public void save(Book book) {
        entityManager.getTransaction().begin();
        entityManager.persist(book);
        entityManager.getTransaction().commit();
    }

    public Book findById(int id) throws BookNotFoundException {
        return entityManager.find(Book.class, id);
    }

    public Book findByTitle(String title) throws BookNotFoundException {
        QBook book = QBook.book;
        Book result = queryFactory
                .selectFrom(book)
                .where(book.title.eq(title))
                .fetchOne();

        if (result == null) {
            throw new BookNotFoundException("Book with title '" + title + "' not found.");
        }

        return result;
    }


    // QueryDSL: get all books
    public List<Book> findAll() {
        QBook b = QBook.book;
        return queryFactory.selectFrom(b).fetch();
    }

}