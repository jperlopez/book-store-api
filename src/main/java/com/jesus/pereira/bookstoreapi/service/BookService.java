package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.domain.Category;

import java.util.List;

public interface BookService {

    Book findBookById(Long id);

    List<Book> findAllBooks();

    Book findBookByName(Long name);

    List<Book> findBooksByNameLike(Long name);

    List<Book> findBooksByAuthor (Author author);

    List<Book> findBooksByCategory(Category category);

    Book createBook(Book bookToPersist, Long categoryId, Long authorId);

    Book updateBook(Book bookToPersist, Long bookId);

    void deleteCategory();
}
