package com.jesus.pereira.bookstoreapi.service;


import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.exception.AuthorAlreadyExistsException;
import com.jesus.pereira.bookstoreapi.exception.BookAlreadyExistsException;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.repository.BookRepository;
import com.jesus.pereira.bookstoreapi.resource.dto.AuthorDTO;
import com.jesus.pereira.bookstoreapi.resource.dto.BookDTO;
import com.jesus.pereira.bookstoreapi.resource.dto.CategoryDTO;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private static final Long bookId = 1L;

    private Category category;
    private Author author;
    private Book book1;
    private Book book2;
    private Book book3;
    private AuthorDTO authorDTO;
    private CategoryDTO categoryDTO;


    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Category1")
                .description("Test1")
                .build();

        author = Author.builder()
                .id(1L)
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

        authorDTO = AuthorDTO.builder()
                .id(1L)
                .build();

        categoryDTO = CategoryDTO.builder()
                .id(1L)
                .build();
    }

    @Test
    void givenAnIdShouldFindBook() {

        book1.setId(bookId);

        given(bookRepository.findById(bookId)).willReturn(Optional.of(book1));
        Book retrievedBook = bookService.findBookById(bookId);

        verify(bookRepository.findById(any()), times(1));
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

        BookDTO bookDto = BookDTO.builder()
                .name("Book1")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .authorId(author.getId())
                .categoryId(category.getId())
                .authorDTO(authorDTO)
                .categoryDTO(categoryDTO)
                .build();

        given(authorService.findAuthorById(bookDto.getAuthorId())).willReturn(author);
        Book createdBook = bookService.createBook(bookDto);
        assertThat(createdBook).isNotNull();
        assertThat(createdBook).usingRecursiveComparison().isEqualTo(book1);
    }

    @Test
    void givenUpdateRequestShouldUpdateBook() {
        Long id = 1L;
        BookDTO bookDto = BookDTO.builder()
                .name("Book1")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .authorId(author.getId())
                .categoryId(category.getId())
                .authorDTO(authorDTO)
                .categoryDTO(categoryDTO)
                .build();

        given(bookRepository.save(book1)).willReturn(book1);
        given(bookRepository.findById(id)).willReturn(Optional.ofNullable(book1));

        Book updatedBook = bookService.updateBook(bookDto, id);

        verify(bookRepository.findById(any()), times(1));
        verify(bookRepository.save(any()), times(1));

        assertThat(updatedBook).isNotNull();
    }

    @Test
    void givenBookIdShouldDeleteBook() {

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        category.setBooks(bookList);
        author.setBooks(bookList);

        given(categoryService.findCategoryById(any())).willReturn(category);
        given(authorService.findAuthorById(any())).willReturn(author);

        bookService.deleteBook(bookId);

        verify(categoryService.findCategoryById(any()), times(1));
        verify(authorService.findAuthorById(any()), times(1));
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void givenBookIdWhichNotExistsShouldReturnException() {
        given(bookRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.findBookById(1L)).isInstanceOf(NoSuchElementExistsException.class);
    }

    @Test
    public void givenSaveRequestWhenBookNameAlreadyExistsThenSaveShouldThrowException() {

        book1.setId(bookId);

        BookDTO bookDto = BookDTO.builder()
                .name("Book1")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .authorId(author.getId())
                .categoryId(category.getId())
                .authorDTO(authorDTO)
                .categoryDTO(categoryDTO)
                .build();

        given(categoryService.findCategoryById(category.getId())).willReturn(category);
        given(authorService.findAuthorById(author.getId())).willReturn(author);
        given(bookRepository.findByNameIgnoreCase(book1.getName())).willReturn(Optional.ofNullable(book1));

        assertThatThrownBy(() -> bookService.createBook(bookDto))
                .isInstanceOf(BookAlreadyExistsException.class);
    }
}
