package org.uthmaniv.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
public class BorrowRecord {
    @Id
    @Column(nullable = false)
    private String id;

    @OneToOne(optional = false)
    private Student student;

    @OneToMany
    private Book book;

    @Column(nullable = false)
    private Instant borrowedAt;

    private Instant returnedAt;

    public BorrowRecord() {
        this.id = UUID.randomUUID().toString();
    }

    public BorrowRecord(Student student, Book book) {
        this(); //calls the no args constructor
        this.student = student;
        this.book = book;
        this.borrowedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public BorrowRecord setId(String id) {
        this.id = id;
        return this;
    }

    public Student getStudent() {
        return student;
    }

    public BorrowRecord setStudent(Student student) {
        this.student = student;
        return this;
    }

    public Book getBook() {
        return book;
    }

    public BorrowRecord setBook(Book book) {
        this.book = book;
        return this;
    }

    public Instant getBorrowedAt() {
        return borrowedAt;
    }

    public BorrowRecord setBorrowedAt(Instant borrowedAt) {
        this.borrowedAt = borrowedAt;
        return this;
    }

    public Instant getReturnedAt() {
        return returnedAt;
    }

    public BorrowRecord setReturnedAt(Instant returnedAt) {
        this.returnedAt = returnedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRecord that = (BorrowRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(student, that.student) && Objects.equals(book, that.book) && Objects.equals(borrowedAt, that.borrowedAt) && Objects.equals(returnedAt, that.returnedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, book, borrowedAt, returnedAt);
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id='" + id + '\'' +
                ", student=" + student +
                ", book=" + book +
                ", borrowedAt=" + borrowedAt +
                ", returnedAt=" + returnedAt +
                '}';
    }
}
