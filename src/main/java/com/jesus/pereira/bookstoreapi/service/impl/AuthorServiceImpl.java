package com.jesus.pereira.bookstoreapi.service.impl;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.exception.AuthorAlreadyExistsException;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.mapper.AuthorMapper;
import com.jesus.pereira.bookstoreapi.repository.AuthorRepository;
import com.jesus.pereira.bookstoreapi.resource.dto.AuthorDTO;
import com.jesus.pereira.bookstoreapi.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private static final String AUTHOR_ALREADY_EXISTS_EXCEPTION = "Author with name %1$s and surname %2$s";
    private static final String NO_SUCH_ELEMENT_EXISTS_EXCEPTION = "No author exists with id %s";

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }


    @Override
    public Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, id)));
    }

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> findAuthorsByNameLike(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Author createAuthor(AuthorDTO authorDto) {
        authorRepository.findByNameAndSurnameIgnoreCase(authorDto.getName(), authorDto.getSurname())
                .ifPresent(err -> {
                    throw new AuthorAlreadyExistsException(String.format(AUTHOR_ALREADY_EXISTS_EXCEPTION, authorDto.getName(), authorDto.getSurname()));
                });
        Author author = authorMapper.toAuthor(authorDto);
        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthor(AuthorDTO authorDto, Long id) {
        Author authorToPersist = authorMapper.toAuthorUpdate(authorDto, id);
        return authorRepository.findById(id)
                .map(author -> authorRepository.save(authorToPersist))
                .orElseThrow(() -> new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, id)));
    }

    @Override
    public void deleteAuthor(Long authorId) {
        authorRepository.findById(authorId).ifPresentOrElse(
                (ok) -> authorRepository.deleteById(authorId),
                () -> {
                    throw new NoSuchElementExistsException(String.format(NO_SUCH_ELEMENT_EXISTS_EXCEPTION, authorId));
                });
    }
}
