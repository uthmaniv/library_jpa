package library_test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.uthmaniv.Book;
import org.uthmaniv.BookRepository;
import org.uthmaniv.exception.BookNotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private BookRepository repository;

    @BeforeAll
    static void setupAll() {
        emf = Persistence.createEntityManagerFactory("libraryPU");
    }

    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
        repository = new BookRepository(em);
    }

    @AfterEach
    void tearDown() {
        if (em.isOpen()) em.close();
    }

    @AfterAll
    static void tearDownAll() {
        if (emf.isOpen()) emf.close();
    }

    @Test
    void testSaveAndFindById() throws BookNotFoundException {
        Book book = new Book(111L, "The Hobbit", "J.R.R. Tolkien", "Fantasy", LocalDate.of(1937, 9, 21));
        repository.save(book);

        Book found = repository.findById(111);
        assertNotNull(found);
        assertEquals("The Hobbit", found.getTitle());
    }

    @Test
    void testFindByTitleSuccess() throws BookNotFoundException {
        Book book = new Book(222L, "1984", "George Orwell", "Dystopian", LocalDate.of(1949, 6, 8));
        repository.save(book);

        Book found = repository.findByTitle("1984");
        assertNotNull(found);
        assertEquals("George Orwell", found.getAuthor());
    }

    @Test
    void testFindByTitleNotFoundThrowsException() {
        assertThrows(BookNotFoundException.class, () -> repository.findByTitle("Unknown Book"));
    }

    @Test
    void testFindAllReturnsAllBooks() {
        repository.save(new Book(333L, "Clean Code", "Robert C. Martin", "Programming", LocalDate.of(2008, 8, 1)));
        repository.save(new Book(444L, "The Pragmatic Programmer", "Andrew Hunt", "Programming", LocalDate.of(1999, 10, 30)));

        List<Book> allBooks = repository.findAll();
        assertEquals(2, allBooks.size());
    }
}
