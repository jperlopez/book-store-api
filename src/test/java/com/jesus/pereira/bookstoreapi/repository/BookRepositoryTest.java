package com.jesus.pereira.bookstoreapi.repository;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.domain.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {


    @Autowired
    BookRepository bookRepository;

    @Autowired
    TestEntityManager entityManager;

    private Book book1;
    private Book book2;
    private Author author;
    private Category category;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .name("Author")
                .surname("Test")
                .build();

        category = Category.builder()
                .name("Category")
                .description("Test category")
                .build();

        entityManager.persistAndFlush(author);
        entityManager.persistAndFlush(category);

        book1 = Book.builder()
                .name("Book1")
                .description("The first book")
                .prize(new BigDecimal("49.99"))
                .pages(100)
                .author(author)
                .category(category)
                .build();

        book2 = Book.builder()
                .name("Book2")
                .description("The second book")
                .prize(new BigDecimal("59.99"))
                .pages(150)
                .author(author)
                .category(category)
                .build();
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldReturnBook() {
        Book bookPersisted = entityManager.persistAndFlush(book1);
        Book bookRetrieved = bookRepository.findById(bookPersisted.getId()).orElse(null);

        assertThat(bookRetrieved).isNotNull();
        assertThat(bookRetrieved).usingRecursiveComparison().isEqualTo(bookPersisted);
    }

    @Test
    void shouldReturnAllBooks() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        entityManager.persistAndFlush(book1);
        entityManager.persistAndFlush(book2);

        List<Book> booksRetrieved = bookRepository.findAll();

        assertThat(booksRetrieved).size().isEqualTo(2);
        assertThat(booksRetrieved).containsAll(bookList);
    }

    @Test
    void shouldReturnBookByName() {
        Book bookPersisted = entityManager.persistAndFlush(book1);
        Book bookRetrieved = bookRepository.findByNameIgnoreCase("Book1").orElse(null);

        assertThat(bookRetrieved).isNotNull();
        assertThat(bookRetrieved).usingRecursiveComparison().isEqualTo(bookPersisted);
    }

    @Test
    void shouldReturnBooksByAuthor() {

        entityManager.persistAndFlush(book1);
        entityManager.persistAndFlush(book2);

        List<Book> booksRetrieved = bookRepository.findByAuthorId(author.getId());

        assertThat(booksRetrieved).size().isEqualTo(2);

    }

    @Test
    void shouldReturnAllBooksByNameLike() {
        Book book3 = Book.builder()
                .name("Comedy")
                .description("Comedy book")
                .prize(new BigDecimal("59.99"))
                .author(author)
                .category(category)
                .build();

        entityManager.persistAndFlush(book1);
        entityManager.persistAndFlush(book2);
        entityManager.persistAndFlush(book3);

        List<Book> booksRetrieved = bookRepository.findAll();
        List<Book> booksRetrievedByName = bookRepository.findByNameContainingIgnoreCase("boo");

        assertThat(booksRetrieved).size().isEqualTo(3);
        assertThat(booksRetrievedByName).size().isEqualTo(2);
    }

    @Test
    void shouldDeleteBook() {
        Long id = entityManager.persistAndFlush(book1).getId();
        entityManager.persistAndFlush(book2);
        bookRepository.deleteById(id);

        List<Book> bookList = bookRepository.findAll();

        assertThat(bookList).size().isEqualTo(1);
        assertThat(bookList).contains(book2);
    }

}
