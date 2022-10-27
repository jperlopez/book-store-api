package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.domain.Category;

import java.util.List;

public interface BookService {

    Book findBookById(Long id);

    Book findBookByName(String name);

    List<Book> findBooksByNameLike(String name);

    List<Book> findBooksByAuthorId(Long authorId);

    List<Book> findBooksByCategoryId(Long categoryId);

    //TODO: Should we use nested resources or instead a DTO containing author id and category???s
    Book createBook(Book bookToPersist, Long categoryId, Long authorId);

    Book updateBook(Book bookToPersist, Long bookId);

    void deleteCategory();

    List<Book> findByCategoryId(Long id);
}
