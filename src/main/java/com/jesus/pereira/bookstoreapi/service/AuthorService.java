package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Category;
import com.jesus.pereira.bookstoreapi.resource.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {

    Author findAuthorById(Long id);

    List<Author> findAllAuthors();

    List<Author> findAuthorsByNameLike(String name);

    Author createAuthor(AuthorDTO authorDTO);

    Author updateAuthor(AuthorDTO authorDTO, Long id);

    void deleteAuthor(Long authorId);


}
