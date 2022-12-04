package com.jesus.pereira.bookstoreapi.service;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.exception.AuthorAlreadyExistsException;
import com.jesus.pereira.bookstoreapi.exception.NoSuchElementExistsException;
import com.jesus.pereira.bookstoreapi.mapper.AuthorMapper;
import com.jesus.pereira.bookstoreapi.repository.AuthorRepository;
import com.jesus.pereira.bookstoreapi.resource.dto.AuthorDTO;
import com.jesus.pereira.bookstoreapi.service.impl.AuthorServiceImpl;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorMapper authorMapper;

    @InjectMocks
    AuthorServiceImpl authorService;

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

        verify(authorRepository, times(1)).findById(any());
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

        verify(authorRepository, times(1)).findAll();
        assertThat(retrievedAuthors).isNotEmpty();
        assertThat(retrievedAuthors).containsAll(authorList);
    }

    @Test
    void givenNameShouldReturnAllAuthorsWithNameLike() {

        String name = "aut";
        List<Author> authorList = new ArrayList<>();
        authorList.add(author1);
        authorList.add(author2);

        given(authorRepository.findByNameContainingIgnoreCase(name)).willReturn(authorList);
        List<Author> retrievedAuthors = authorService.findAuthorsByNameLike(name);

        assertThat(retrievedAuthors).isNotEmpty();
        assertThat(retrievedAuthors).size().isEqualTo(2);
    }

    @Test
    void givenSaveRequestShouldSaveAuthor() {
        AuthorDTO authorDTO = AuthorDTO.builder()
                .name("AuthorDTO")
                .surname("Test4")
                .build();
        Author authorToPersist = Author.builder()
                .name("AuthorDTO")
                .surname("Test4")
                .build();

        given(authorRepository.findByNameAndSurnameIgnoreCase(authorDTO.getName(), authorDTO.getSurname()))
                .willReturn(Optional.empty());
        given(authorMapper.toAuthor(authorDTO)).willReturn(authorToPersist);
        given(authorRepository.save(authorToPersist)).willAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0));
        Author authorPersisted = authorService.createAuthor(authorDTO);

        verify(authorRepository, times(1)).save(any());
        assertThat(authorPersisted).isNotNull();
        assertThat(authorPersisted).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(authorToPersist);
    }

    @Test
    void givenUpdateRequestAndIdShouldUpdateAuthor() {

        final Long id = 1L;

        AuthorDTO authorDTO = AuthorDTO.builder()
                .name("AuthorDTO")
                .surname("Test4")
                .build();
        Author authorToPersist = Author.builder()
                .id(1L)
                .name("AuthorDTO")
                .surname("Test4")
                .build();

        given(authorMapper.toAuthorUpdate(authorDTO, id)).willReturn(authorToPersist);
        given(authorRepository.findById(id)).willReturn(Optional.of(authorToPersist));
        given(authorRepository.save(authorToPersist)).willReturn(authorToPersist);

        final Author authorUpdated = authorService.updateAuthor(authorDTO, id);

        verify(authorRepository, times(1)).findById(any());
        verify(authorRepository,times(1)).save(any());
        assertThat(authorUpdated).usingRecursiveComparison().ignoringFields("id")
                .isNotEqualTo(author1);
    }

    @Test
    void givenAnIdShouldDeleteAuthor() {
        final Long id = 1L;
        author1.setId(id);
        given(authorRepository.findById(id)).willReturn(Optional.of(author1));

        authorService.deleteAuthor(id);

        verify(authorRepository, times(1)).findById(any());
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenAnAuthorIdNotPersistedShouldThrowException() {
        Long id = 1L;
        given(authorRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findAuthorById(id))
                .isInstanceOf(NoSuchElementExistsException.class);
    }

    @Test
    void givenAnAuthorAlreadyExistsWhenSavingShouldThenShouldThrowException() {

        AuthorDTO authorDTO = AuthorDTO.builder()
                .name("AuthorDTO")
                .surname("Test4")
                .build();

        given(authorRepository.findByNameAndSurnameIgnoreCase(any(), any())).willReturn(Optional.of(author1));
        assertThatThrownBy(() -> authorService.createAuthor(authorDTO))
                .isInstanceOf(AuthorAlreadyExistsException.class);
    }
}
