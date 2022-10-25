package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    AuthorService authorService;

    private Author author1;
    private Author author2;
    private Author author3;

    @BeforeEach
    void setUp() {
        author1 = Author.builder()
                .name("Author1")
                .surname("Test1")
                .build();

        author2 = Author.builder()
                .name("Author2")
                .surname("Test2")
                .build();

        author3 = Author.builder()
                .name("Jhon")
                .surname("Doe")
                .build();
    }

    @Test
    void givenAnIdShouldReturnAnAuthor() {
        Long id = 1L;
        author1.setId(1L);

        given(authorRepository.findById(id)).willReturn(Optional.of(author1));
        Author retrievedAuthor = authorService.findAuthorById(id);

        verify(authorRepository.findById(any()), times(1));
        assertThat(retrievedAuthor).isNotNull();
        assertThat(retrievedAuthor).usingRecursiveComparison().isEqualTo(author1);
    }

    @Test
    void givenSearchRequestShouldReturnAllAuthors() {

        List<Author> authorList = new ArrayList<>();
        authorList.add(author1);
        authorList.add(author2);
        authorList.add(author3);

        given(authorRepository.findAll()).willReturn(authorList);
        List<Author> retrievedAuthors = authorService.findAllAuthors();

        verify(authorRepository.findAll(), times(1));
        assertThat(retrievedAuthors).isNotEmpty();
        assertThat(retrievedAuthors).containsAll(authorList);
    }

    @Test
    void givenNameShouldReturnAuthor() {

        given(authorRepository.findByNameIgnoreCase(author1.getName())).willReturn(Optional.of(author1));
        Author retrievedAuthor = authorService.findAuthorByName(author1.getName());

        verify(authorRepository.findByNameIgnoreCase(any()), times(1));
        assertThat(retrievedAuthor).isNotNull();
        assertThat(retrievedAuthor).usingRecursiveComparison().isEqualTo(author1);
    }

    @Test
    void givenNameShouldReturnAllAuthorsWithNameLike() {

        String name = "aut";
        List<Author> authorList = new ArrayList<>();
        authorList.add(author1);
        authorList.add(author2);
        authorList.add(author3);

        given(authorRepository.findByNameContainingIgnoreCase(name)).willReturn(authorList);
        List<Author> retrievedAuthors = authorService.findAuthorsByNameLike(name);

        verify(authorRepository.findAll(), times(1));
        assertThat(retrievedAuthors).isNotEmpty();
        assertThat(retrievedAuthors).size().isEqualTo(2);
    }

    @Test
    void givenSaveRequestShouldSaveAuthor() {
        Author authorToPersist = Author.builder()
                .name("Author")
                .surname("Test1")
                .build();

        given(authorRepository.save(authorToPersist)).willAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        Author authorPersisted = authorService.createAuthor(authorToPersist);

        verify(authorRepository.save(any()), times(1));
        assertThat(authorPersisted).isNotNull();
        assertThat(authorPersisted).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(authorToPersist);
    }

    @Test
    void givenUpdateRequestAndIdShouldUpdateAuthor() {
        final Long id = 1L;
        author1.setId(id);
        given(authorRepository.findById(id)).willReturn(Optional.of(author1));

        final Author authorUpdated = authorService.updateAuthor(author1);

        verify(authorRepository.findById(any()), times(1));
        verify(authorRepository.save(any()), times(1));
        assertThat(authorUpdated).usingRecursiveComparison().isEqualTo(author1);
    }

    @Test
    void givenAnIdShouldDeleteAuthor() {
        final Long id = 1L;
        author1.setId(id);
        given(authorRepository.findById(id)).willReturn(Optional.of(author1));

        authorService.deleteAuthor();

        verify(authorRepository.findById(any()), times(1));
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenAnAuthorIdNotPersistedShouldThrowException() {
        Long id = 1L;
        given(authorRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findAuthorById(id))
                .isInstanceOf(NoSuchElementExistsException.class);
    }
}
