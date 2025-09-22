package library_test;

import org.junit.jupiter.api.*;
import jakarta.persistence.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.uthmaniv.Student;
import org.uthmaniv.StudentRepository;
import org.uthmaniv.exception.StudentNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private StudentRepository repository;

    @BeforeAll
    static void setupEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("libraryTestPU");
    }

    @BeforeEach
    void setup() {
        em = emf.createEntityManager();
        repository = new StudentRepository(em, new JPAQueryFactory(em));
    }

    @AfterEach
    void tearDown() {
        if (em.isOpen()) em.close();
    }

    @AfterAll
    static void closeEntityManagerFactory() {
        if (emf.isOpen()) emf.close();
    }

    @Test
    void testSaveAndFindById() {
        Student s = new Student("Usman", "Yahaya");

        repository.save(s);

        Student found = repository.findById("1");
        assertNotNull(found);
        assertEquals("John", found.getFirstName());
    }

    @Test
    void testFindByName_Success() throws Exception {
        Student s = new Student("Anas","Yakubu");
        repository.save(s);

        Student found = repository.findByName("Anas", "Yakubu");

        assertNotNull(found);
        assertEquals("Alice", found.getFirstName());
        assertEquals("Smith", found.getLastName());
    }

    @Test
    void testFindByName_NotFound() {
        assertThrows(StudentNotFoundException.class, () -> {
            repository.findByName("Nonexistent", "Student");
        });
    }
}
