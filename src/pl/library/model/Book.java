package pl.library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Book {

    private String title;

    private String author;

    private String isbn;

    private LocalDate releaseDate;

    public Book() {
    }

    public Book(String title, String author, String isbn, LocalDate releaseDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(getTitle(), book.getTitle()) &&
                Objects.equals(getAuthor(), book.getAuthor()) &&
                Objects.equals(getIsbn(), book.getIsbn()) &&
                Objects.equals(getReleaseDate(), book.getReleaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAuthor(), getIsbn(), getReleaseDate());
    }

    @Override
    public String toString() {
        return "Tytuł: " + title + '\'' +
                ", autor: " + author + '\'' +
                ", isbn: " + isbn + '\'' +
                ", releaseDate: " + releaseDate;
    }

    public static class Builder
    {
        Book book = new Book();

        public Builder withTitle(String title) {
            book.title = title;
            return this;
        }

        public Builder withAuthor(String author) {
            book.author = author;
            return this;
        }

        public Builder withIsbn(String isbn) {
            book.isbn = isbn;
            return this;
        }

        public Builder withReleaseDate(LocalDate releaseDate) {
            book.releaseDate = releaseDate;
            return this;
        }

        public Book build()
        {
            Book bookCopy = book;
            book = new Book();
            return bookCopy;
        }

    }



}
