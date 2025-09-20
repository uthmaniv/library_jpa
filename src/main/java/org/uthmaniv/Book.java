package org.uthmaniv;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @Column
    private  long isbn;

    @Column
    private  String title;

    @Column
    private  String author;

    @Column
    private  String genre;

    @Column
    private  LocalDate publicationYear;


    public Book(int id, long isbn, String title, String author, String genre, LocalDate publicationYear) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
    }

    public int getId() {
        return id;
    }

    public Book setId(int id) {
        this.id = id;
        return this;
    }

    public long getIsbn() {
        return isbn;
    }

    public Book setIsbn(long isbn) {
        this.isbn = isbn;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Book setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public LocalDate getPublicationYear() {
        return publicationYear;
    }

    public Book setPublicationYear(LocalDate publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && isbn == book.isbn && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre) && Objects.equals(publicationYear, book.publicationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, title, author, genre, publicationYear);
    }

    @Override
    public String toString() {
        return "org.uthmaniv.Book{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", publicationYear=" + publicationYear +
                '}';
    }
}
