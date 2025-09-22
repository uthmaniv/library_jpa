package org.uthmaniv;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.uthmaniv.exception.BookNotFoundException;
import org.uthmaniv.exception.StudentNotFoundException;

import java.time.LocalDate;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws BookNotFoundException, StudentNotFoundException {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        LibraryService service = new LibraryService(queryFactory, em);

        service.createStudent("Usman","Yahaya");

        var b1 = service.createBook(
                333342L,
                "The Hobbit",
                "J.R.R. Tolkien",
                "Fantasy",
                LocalDate.of(1937, 9, 21)
        );

        var b2 = service.createBook(
                555112233L,
                "1984",
                "George Orwell",
                "Dystopian",
                LocalDate.of(1949, 6, 8)
        );

        var b3 = service.createBook(
                777889900L,
                "To Kill a Mockingbird",
                "Harper Lee",
                "Fiction",
                LocalDate.of(1960, 7, 11)
        );

        var b4 = service.createBook(
                999443322L,
                "The Catcher in the Rye",
                "J.D. Salinger",
                "Fiction",
                LocalDate.of(1951, 7, 16)
        );

        service.getAllBooks().forEach(b -> logger.info("Book: {} by {}", b.getTitle(), b.getAuthor()));

        String res = service.borrowBook("Usman Yahaya", b1.getTitle());
        logger.info("Borrow result: {}", res);

        String res2 = service.borrowBook("Anas Yakubu", b2.getTitle());
        logger.info("Borrow attempt 2 result: {}", res2);

        String r = service.returnBook("Usman Yahaya");
        logger.info("Return result: {}", r);

        String r3 = service.borrowBook("Usman Yahaya", b2.getTitle());
        logger.info("Borrow 2 after return: {}", r3);

        em.close();
        JPAUtil.shutdown();
    }
}