package library_test;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.uthmaniv.Book;
import org.uthmaniv.BorrowRecord;
import org.uthmaniv.LibraryService;
import org.uthmaniv.Student;
import org.uthmaniv.exception.BookNotFoundException;
import org.uthmaniv.exception.StudentNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager em;
    private LibraryService service;

    @BeforeAll
    static void initAll() {
        entityManagerFactory = Persistence.createEntityManagerFactory("libraryPU");
    }

    @BeforeEach
    void init() {
        em = entityManagerFactory.createEntityManager();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        service = new LibraryService(queryFactory, em);
    }

    @AfterEach
    void tearDown() {
        if (em.isOpen()) em.close();
    }

    @AfterAll
    static void destroyAll() {
        if (entityManagerFactory.isOpen()) entityManagerFactory.close();
    }

    @Test
    void testCreateAndFetchBook() {
        service.createBook(111L, "The Hobbit", "J.R.R. Tolkien", "Fantasy", LocalDate.of(1937, 9, 21));

        List<Book> books = service.getAllBooks();
        assertEquals(1, books.size());
        assertEquals("The Hobbit", books.getFirst().getTitle());
    }

    @Test
    void testCreateStudent() throws StudentNotFoundException {
        service.createStudent("Usman", "Yahaya");

        service.createStudent("Usman", "Yahaya");

        Student s = em.createQuery("SELECT s FROM Student s WHERE s.firstName='Usman' AND s.lastName='Yahaya'", Student.class)
                .getSingleResult();
        assertNotNull(s);
        assertEquals("John Doe", s.getFullName());
    }

    @Test
    void testBorrowBookSuccessfully() throws Exception {
        service.createStudent("Anas", "Yakubu");
        service.createBook(222L, "1984", "George Orwell", "Dystopian", LocalDate.of(1949, 6, 8));

        String result = service.borrowBook("Anas Yakubu", "1984");
        assertEquals("Book issued successfully.", result);

        BorrowRecord record = em.createQuery("SELECT b FROM BorrowRecord b", BorrowRecord.class)
                .getSingleResult();
        assertEquals("1984", record.getBook().getTitle());
        assertNull(record.getReturnedAt());
    }

    @Test
    void testBorrowBookFailsIfAlreadyBorrowed() throws Exception {
        service.createStudent("Ibrahim", "Sheme");
        service.createBook(333L, "Clean Code", "Robert Martin", "Programming", LocalDate.of(2008, 8, 1));

        service.borrowBook("Ibrahim Yakubu", "Clean Code");
        String result = service.borrowBook("Ibrahim Yakubu", "Clean Code");

        assertTrue(result.contains("You must return the previously borrowed book"));
    }

    @Test
    void testBorrowBookFailsIfStudentNotFound() throws BookNotFoundException, StudentNotFoundException {
        service.createBook(444L, "The Alchemist", "Paulo Coelho", "Philosophy", LocalDate.of(1988, 4, 1));

        String result = service.borrowBook("Mahadi Abuhuraira", "The Alchemist");
        assertEquals("Student not found. Please register first.", result);
    }

    @Test
    void testReturnBookSuccessfully() throws Exception {
        service.createStudent("Mustapha", "Sanusi");
        service.createBook(555L, "The Pragmatic Programmer", "Hunt & Thomas", "Programming", LocalDate.of(1999, 10, 30));

        service.borrowBook("Mustapha Sanusi", "The Pragmatic Programmer");
        String result = service.returnBook("Carol White");

        assertEquals("Book returned successfully.", result);

        BorrowRecord record = em.createQuery("SELECT b FROM BorrowRecord b", BorrowRecord.class)
                .getSingleResult();
        assertNotNull(record.getReturnedAt());
    }

    @Test
    void testReturnBookFailsIfNoActiveBorrow() throws StudentNotFoundException {
        service.createStudent("Majidon", "Sunusi");

        String result = service.returnBook("Majidon Sunusi");
        assertEquals("No borrowed book to return.", result);
    }

    @Test
    void testReturnBookFailsIfStudentNotFound() throws StudentNotFoundException {
        String result = service.returnBook("Isa Amina");
        assertEquals("Student not found.", result);
    }
}
