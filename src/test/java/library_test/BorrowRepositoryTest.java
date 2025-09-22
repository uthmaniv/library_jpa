package library_test;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.uthmaniv.Book;
import org.uthmaniv.BorrowRecord;
import org.uthmaniv.BorrowRepository;
import org.uthmaniv.Student;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BorrowRepositoryTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private BorrowRepository borrowRepository;
    private Student student;
    private Book book;

    @BeforeAll
    static void init() {
        emf = Persistence.createEntityManagerFactory("libraryTestPU");
    }

    @AfterAll
    static void close() {
        emf.close();
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        borrowRepository = new BorrowRepository(em);

        // create test Student + Book
        em.getTransaction().begin();
        student = new Student("Usman", "Yahaya");
        book = new Book(123456789L, "The Hobbit", "J.R.R. Tolkien", "Fantasy", null);
        em.persist(student);
        em.persist(book);
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    void testSave() {
        BorrowRecord record = new BorrowRecord(student, book);

        borrowRepository.save(record);

        BorrowRecord found = em.find(BorrowRecord.class, record.getId());
        assertNotNull(found);
        assertEquals(student.getId(), found.getStudent().getId());
        assertEquals(book.getId(), found.getBook().getId());
    }

    @Test
    void testUpdate() {
        BorrowRecord record = new BorrowRecord(student, book);
        borrowRepository.save(record);

        record.setReturnedAt(Instant.now());
        borrowRepository.update(record);

        BorrowRecord updated = em.find(BorrowRecord.class, record.getId());
        assertNotNull(updated.getReturnedAt());
    }

    @Test
    void testFindActiveByStudent() {
        BorrowRecord record = new BorrowRecord(student, book);
        borrowRepository.save(record);

        BorrowRecord active = borrowRepository.findActiveByStudent(student);
        assertNotNull(active);
        assertEquals(student.getId(), active.getStudent().getId());

        record.setReturnedAt(Instant.now());
        borrowRepository.update(record);

        BorrowRecord none = borrowRepository.findActiveByStudent(student);
        assertNull(none);
    }

    @Test
    void testFindAll() {
        BorrowRecord r1 = new BorrowRecord(student, book);
        BorrowRecord r2 = new BorrowRecord(student, book);

        borrowRepository.save(r1);
        borrowRepository.save(r2);

        List<BorrowRecord> all = borrowRepository.findAll();
        assertEquals(2, all.size());
    }
}
