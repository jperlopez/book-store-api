package com.jesus.pereira.bookstoreapi.service.impl;

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
import com.jesus.pereira.bookstoreapi.service.AuthorService;
import com.jesus.pereira.bookstoreapi.service.BookService;
import com.jesus.pereira.bookstoreapi.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOK_ALREADY_EXISTS_EXCEPTION = "Book with name %1$s ";
    private static final String NO_SUCH_ELEMENT_EXISTS_EXCEPTION = "No book exists with id %s";

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, AuthorService authorService,
                           AuthorMapper authorMapper, CategoryService categoryService, CategoryMapper categoryMapper){

        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorService = authorService;
        this.authorMapper = authorMapper;
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION,id)));
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findBooksByNameLike(String name) {
        return bookRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Book> findBooksByAuthorId(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    @Override
    public List<Book> findBooksByCategoryId(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    @Override
    public Book createBook(BookDTO bookDTO) {
        bookRepository.findByNameIgnoreCase(bookDTO.getName())
            .ifPresent(error -> {
                throw new BookAlreadyExistsException(String.format(BOOK_ALREADY_EXISTS_EXCEPTION, bookDTO.getName()));
            });
        return bookRepository.save(buildBook(bookDTO, null));
    }

    @Override
    public Book updateBook(BookDTO bookRequest, Long bookId) {
        Book bookToUpdate = buildBook(bookRequest, bookId);
        return bookRepository.findById(bookId)
                .map(book -> bookRepository.save(bookToUpdate))
                .orElseThrow(() -> new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, bookId)));
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId) {
         bookRepository.findById(bookId)
                .ifPresentOrElse(
                        this::removeBookFromParentsAndDelete,
                        () -> {
                            throw new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, bookId));
                        }
                );
    }

    private void removeBookFromParentsAndDelete(Book book) {
        removeBookFromCategory(book);
        removeBookFromAuthor(book);
        bookRepository.deleteById(book.getId());
    }

    private void removeBookFromCategory(Book book) {
        Category category = categoryService.findCategoryById(book.getCategory().getId());
        category.getBooks().remove(book);
        CategoryDTO categoryDTO = categoryMapper.toCategoryDto(category);
        categoryService.updateCategory(categoryDTO, category.getId());
    }

    private void removeBookFromAuthor(Book book) {
        Author author = authorService.findAuthorById(book.getAuthor().getId());
        author.getBooks().remove(book);
        AuthorDTO authorDTO = authorMapper.toAuthorDto(author);
        authorService.updateAuthor(authorDTO, author.getId());
    }

    private Book buildBook(BookDTO bookDTO, Long id) {
        Book book = id != null ? bookMapper.toBookUpdate(bookDTO, id) : bookMapper.toBook(bookDTO);

        book.setAuthor(authorService.findAuthorById(bookDTO.getId()));
        book.setCategory(categoryService.findCategoryById(bookDTO.getCategoryId()));
        book.setCreationDate(LocalDateTime.now());
        book.setUpdateDate(id != null ? LocalDateTime.now() : null);

        return book;
    }
}
