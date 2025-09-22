package org.uthmaniv;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class LibraryService {
    private static final Logger logger = LogManager.getLogger(LibraryService.class);

    private final BookRepository bookRepo;
    private final StudentRepository studentRepo;
    private final BorrowRepository borrowRepo;
    private final EntityManager em;

    public LibraryService(EntityManager em) {
        this.em = em;
        this.bookRepo = new BookRepository(em);
        this.studentRepo = new StudentRepository(em);
        this.borrowRepo = new BorrowRepository(em);
    }

    public Student createStudent(String firstName, String lastName) {
        Student existing = studentRepo.findByName(firstName, lastName);
        if (existing != null) {
            logger.info("Student '{}' already exists with id {}", existing.getFullName(), existing.getId());
            return existing;
        }
        Student s = new Student(firstName, lastName);
        studentRepo.save(s);
        logger.info("Created student: {} ({})", s.getFullName(), s.getId());
        return s;
    }

    public Book createBook(long isbn, String title, String author, String genre, LocalDate publicationYear) {
        Book b = new Book(isbn, title, author, genre, publicationYear);
        bookRepo.save(b);
        logger.info("Created book: {} by {} ({})", title, author, b.getId());
        return b;
    }

    // QueryDSL-based retrieval of all books
    public List<Book> getAllBooks() {
        List<Book> books = bookRepo.findAll();
        logger.info("Fetched {} books", books.size());
        return books;
    }

    // Borrow book: accepts studentName and bookId (or book title as convenience)
    public String borrowBook(String fullName, String title) {
        String[] fullNameArray = fullName.split(" ");

        Student student = studentRepo.findByName(fullNameArray[0],fullNameArray[1]);
        if (student == null) {
            logger.info("Borrow attempt failed: student '{}' not found", fullName);
            return "Student not found. Please register first.";
        }

        BorrowRecord active = borrowRepo.findActiveByStudent(student);
        if (active != null) {
            String msg = String.format("You must return the previously borrowed book ('%s') before borrowing another.", active.getBook().getTitle());
            logger.info("Borrow attempt blocked for student {}: {}", fullName, msg);
            return msg;
        }

        Book book = bookRepo.findByTitle(title);
        if (book == null) {
            logger.info("Borrow attempt failed: book {} not found", title);
            return "Requested book not found.";
        }

        BorrowRecord br = new BorrowRecord(student, book);
        borrowRepo.save(br);
        logger.info("Book '{}' borrowed by student '{}' at {}", book.getTitle(), student.getFullName(), br.getBorrowedAt());
        return "Book issued successfully.";
    }

    // Return book for student (by name)
    public String returnBook(String fullName) {
        String[] fullNameArray = fullName.split(" ");

        Student student = studentRepo.findByName(fullNameArray[0], fullNameArray[1]);
        if (student == null) {
            logger.info("Return attempt failed: student '{}' not found", fullName);
            return "Student not found.";
        }
        BorrowRecord active = borrowRepo.findActiveByStudent(student);
        if (active == null) {
            logger.info("Return attempt: no active borrow found for {}", fullName);
            return "No borrowed book to return.";
        }
        active.setReturnedAt(Instant.now());
        borrowRepo.update(active);
        logger.info("Student '{}' returned book '{}'", fullName, active.getBook().getTitle());
        return "Book returned successfully.";
    }
}

