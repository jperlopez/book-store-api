package com.jesus.pereira.bookstoreapi.service;


import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.exception.BookAlreadyExistsException;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.mapper.AuthorMapper;
import com.jesus.pereira.bookstoreapi.mapper.BookMapper;
import com.jesus.pereira.bookstoreapi.mapper.CategoryMapper;
import com.jesus.pereira.bookstoreapi.repository.BookRepository;
import com.jesus.pereira.bookstoreapi.resource.dto.AuthorDTO;
import com.jesus.pereira.bookstoreapi.resource.dto.BookDTO;
import com.jesus.pereira.bookstoreapi.resource.dto.CategoryDTO;
import com.jesus.pereira.bookstoreapi.service.impl.BookServiceImpl;
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

    @Mock
    BookMapper bookMapper;

    @Mock
    CategoryMapper categoryMapper;

    @Mock
    AuthorMapper authorMapper;

    @InjectMocks
    BookServiceImpl bookService;

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
    }

    @Test
    void givenAnIdShouldFindBook() {

        book1.setId(bookId);

        given(bookRepository.findById(bookId)).willReturn(Optional.of(book1));
        Book retrievedBook = bookService.findBookById(bookId);

        verify(bookRepository, times(1)).findById(any());
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

        verify(bookRepository, times(1)).findByAuthorId(any());
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
        List<Book> retrievedBooks = bookService.findBooksByCategoryId(category.getId());

        verify(bookRepository, times(1)).findByCategoryId(1L);
        assertThat(retrievedBooks).isNotEmpty();
        assertThat(retrievedBooks).containsAll(bookList);
    }


    @Test
    void givenAuthorIdAndBookRequestShouldSaveBook() {

        BookDTO bookDto = BookDTO.builder()
                .name("BookToPersist")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .authorId(1L)
                .categoryId(1L)
                .build();

        Book bookToPersist = Book.builder()
                .id(1L)
                .name("BookToPersist")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .author(author)
                .category(category)
                .build();

        given(bookRepository.findByNameIgnoreCase(bookDto.getName())).willReturn(Optional.empty());
        given(bookMapper.toBook(bookDto)).willReturn(bookToPersist);
        given(authorService.findAuthorById(any())).willReturn(author);
        given(categoryService.findCategoryById(any())).willReturn(category);
        given(bookRepository.save(bookToPersist)).willAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));

        Book createdBook = bookService.createBook(bookDto);

        assertThat(createdBook).isNotNull();
        assertThat(createdBook).usingRecursiveComparison().isEqualTo(bookToPersist);
    }

    @Test
    void givenUpdateRequestShouldUpdateBook() {
        Long id = 1L;

        BookDTO bookDto = BookDTO.builder()
                .name("BookToPersist")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .authorId(1L)
                .categoryId(1L)
                .build();

        Book bookToUpdate = Book.builder()
                .id(1L)
                .name("BookToPersist")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .author(author)
                .category(category)
                .creationDate(LocalDateTime.now())
                .build();

        given(bookRepository.findById(id)).willReturn(Optional.of(bookToUpdate));
        given(bookMapper.toBookUpdate(bookDto, id)).willReturn(bookToUpdate);
        given(authorService.findAuthorById(any())).willReturn(author);
        given(categoryService.findCategoryById(any())).willReturn(category);
        given(bookRepository.save(bookToUpdate)).willReturn(bookToUpdate);

        Book updatedBook = bookService.updateBook(bookDto, id);

        verify(bookRepository, times(1)).findById(any());
        verify(bookRepository, times(1)).save(any());

        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook).usingRecursiveComparison()
                .ignoringFields("updateDate")
                .isEqualTo(bookToUpdate);
        assertThat(updatedBook.getUpdateDate()).isNotNull();
    }

    @Test
    void givenBookIdShouldDeleteBook() {

        BookDTO bookDto = BookDTO.builder()
                .name("BookToPersist")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .authorId(1L)
                .categoryId(1L)
                .build();

        Book bookToDelete = Book.builder()
                .id(1L)
                .name("BookToPersist")
                .description("First book")
                .prize(new BigDecimal("39.99"))
                .pages(100)
                .author(author)
                .category(category)
                .build();

        List<Book> bookList = new ArrayList<>();
        bookList.add(bookToDelete);
        category.setBooks(bookList);
        author.setBooks(bookList);

        given(bookRepository.findById(1L)).willReturn(Optional.ofNullable(bookToDelete));
        given(categoryService.findCategoryById(any())).willReturn(category);
        given(authorService.findAuthorById(any())).willReturn(author);
        given(categoryMapper.toCategoryDto(category)).willReturn(categoryDTO);
        given(authorMapper.toAuthorDto(author)).willReturn(authorDTO);

        bookService.deleteBook(bookId);

        verify(categoryService, times(1)).findCategoryById(any());
        verify(authorService, times(1)).findAuthorById(any());
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void givenBookIdWhichNotExistsShouldReturnException() {
        given(bookRepository.findById(1L)).willReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.findBookById(1L)).isInstanceOf(NoSuchElementExistsException.class);
    }
}
