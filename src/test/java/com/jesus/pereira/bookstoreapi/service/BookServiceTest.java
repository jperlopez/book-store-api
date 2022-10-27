package com.jesus.pereira.bookstoreapi.service;


import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.exception.AuthorAlreadyExistsException;
import com.jesus.pereira.bookstoreapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    CategoryService categoryService;

    @Mock
    AuthorService authorService;

    @InjectMocks
    BookService bookService;

    private Category category;
    private Author author;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp(){
        category = Category.builder()
                .name("Category1")
                .description("Test1")
                .build();

        author = Author.builder()
                .name("Author1")
                .surname("Test")
                .build();

        book1 = Book.builder()
                .name("Book1")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .creationDate(LocalDateTime.now())
                .category(category)
                .author(author)
                .build();

        book2 = Book.builder()
                .name("Book2")
                .description("Second book")
                .prize(new BigDecimal("29.99"))
                .pages(130)
                .creationDate(LocalDateTime.now())
                .category(category)
                .author(author)
                .build();

        book3 = Book.builder()
                .name("Ulises")
                .description("A good book")
                .prize(new BigDecimal("29.99"))
                .pages(200)
                .creationDate(LocalDateTime.now())
                .category(category)
                .author(author)
                .build();
    }

    @Test
    void givenAnIdShouldFindBook(){
        Long id = 1L;
        book1.setId(1L);

        given(bookRepository.findById(id)).willReturn(Optional.of(book1));
        Book retrievedBook = bookService.findBookById(id);

        verify(bookRepository.findById(any()),times(1));
        assertThat(retrievedBook).isNotNull();
        assertThat(retrievedBook).usingRecursiveComparison().isEqualTo(book1);
    }

    @Test
    void givenAuthorIdShouldReturnAllBooks() {

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);

        given(bookRepository.findByAuthorId(author.getId())).willReturn(bookList);
        List<Book> retrievedBooks = bookService.findBooksByAuthorId(author.getId());

        verify(bookRepository.findByAuthorId(any()), times(1));
        assertThat(retrievedBooks).isNotEmpty();
        assertThat(retrievedBooks).containsAll(bookList);
    }

    @Test
    void givenCategoryIdShouldReturnAllBooks() {

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);

        given(bookRepository.findByCategoryId(category.getId())).willReturn(bookList);
        List<Book> retrievedBooks = bookService.findByCategoryId(category.getId());

        verify(bookRepository.findAll(), times(1));
        assertThat(retrievedBooks).isNotEmpty();
        assertThat(retrievedBooks).containsAll(bookList);
    }


    @Test
    void givenNameShouldReturnBook() {

        given(bookRepository.findByNameIgnoreCase(book1.getName())).willReturn(Optional.of(book1));
        Book retrievedBook = bookService.findBookByName(book1.getName());

        verify(bookRepository.findByNameIgnoreCase(any()), times(1));
        assertThat(retrievedBook).isNotNull();
        assertThat(retrievedBook).usingRecursiveComparison().isEqualTo(book1);
    }

    @Test
    void givenNameShouldReturnAllAuthorsWithNameLike() {

        String name = "boo";

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        given(bookRepository.findByNameContainingIgnoreCase(name)).willReturn(bookList);
        List<Book> retrievedBooks = bookService.findBooksByNameLike(name);

        verify(bookRepository.findAll(), times(1));
        assertThat(retrievedBooks).isNotEmpty();
        assertThat(retrievedBooks).size().isEqualTo(2);
    }

    @Test
    void givenAuthorIdAndBookRequestShouldSaveBook() {

        Book book = Book.builder()
                .name("Test")
                .description("A test book")
                .prize(new BigDecimal("29.99"))
                .pages(200)
                .creationDate(LocalDateTime.now())
                .category(category)
                .author(author)
                .build();

        given(authorService.findAuthorById(author.getId())).willReturn(author);
        //Book createdBook = bookService.createBook(book);
    }
}
