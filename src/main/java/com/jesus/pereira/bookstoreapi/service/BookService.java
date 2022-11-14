package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Book;
import com.jesus.pereira.bookstoreapi.resource.dto.BookDTO;


import java.util.List;

public interface BookService {

    Book findBookById(Long id);

    Book findBookByName(String name);

    List<Book> findBooksByNameLike(String name);

    List<Book> findBooksByAuthorId(Long authorId);

    List<Book> findBooksByCategoryId(Long categoryId);

    Book createBook(BookDTO bookRequest);

    Book updateBook(BookDTO bookRequest, Long bookId);

    void deleteBook(Long bookId);

    List<Book> findByCategoryId(Long id);
}
