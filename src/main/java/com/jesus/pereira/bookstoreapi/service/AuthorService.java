package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Category;

import java.util.List;

public interface AuthorService {

    Author findAuthorById(Long id);

    List<Author> findAllAuthors();

    Author findAuthorByName(String name);

    List<Author> findAuthorsByNameLike(String name);

    Author createAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthor();


}
