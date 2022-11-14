package com.jesus.pereira.bookstoreapi.repository;

import com.jesus.pereira.bookstoreapi.domain.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Author author1;
    private Author author2;

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
    }

    @AfterEach
    public void tearDown() {
        authorRepository.deleteAll();
    }

    @Test
    void shouldReturnAnAuthorById() {
        Author authorToPersist = entityManager.persistAndFlush(author1);
        Author authorRetrieved = authorRepository.findById(authorToPersist.getId()).orElse(null);

        assertThat(authorRetrieved).isNotNull();
        assertThat(authorRetrieved).usingRecursiveComparison()
                .isEqualTo(authorToPersist);
    }

    @Test
    void shouldReturnAllAuthors() {

        List<Author> authorList = new ArrayList<>();
        authorList.add(author1);
        authorList.add(author2);

        entityManager.persistAndFlush(author1);
        entityManager.persistAndFlush(author2);

        List<Author> authorsRetrieved = authorRepository.findAll();

        assertThat(authorsRetrieved).size().isEqualTo(2);
        assertThat(authorsRetrieved).containsAll(authorList);
    }


    @Test
    void shouldReturnAuthorByName() {
        Author authorToPersist = entityManager.persistAndFlush(author1);
        Author authorRetrieved = authorRepository.findByNameIgnoreCase("Author1").orElse(null);

        assertThat(authorRetrieved).isNotNull();
        assertThat(authorRetrieved).usingRecursiveComparison()
                .isEqualTo(authorToPersist);
    }

    @Test
    void shouldReturnAuthorByNameAndSurname() {
        Author authorToPersist = entityManager.persistAndFlush(author1);
        Author authorRetrieved = authorRepository.findByNameAndSurnameIgnoreCase("Author1", "Test1").orElse(null);

        assertThat(authorRetrieved).isNotNull();
        assertThat(authorRetrieved).usingRecursiveComparison()
                .isEqualTo(authorToPersist);
    }

    @Test
    void shouldReturnAuthorsByNameLike() {
        List<Author> authorList = new ArrayList<>();
        Author author3 = Author.builder()
                .name("John")
                .surname("Doe")
                .build();

        authorList.add(author1);
        authorList.add(author2);
        authorList.add(author3);

        entityManager.persistAndFlush(author1);
        entityManager.persistAndFlush(author2);
        entityManager.persistAndFlush(author3);

        List<Author> authorsRetrieved = authorRepository.findAll();
        List<Author> authorsRetrievedByName = authorRepository.findByNameContainingIgnoreCase("Aut");

        assertThat(authorsRetrieved).size().isEqualTo(3);
        assertThat(authorsRetrievedByName).size().isEqualTo(2);
    }

    @Test
    void shouldDeleteAnAuthor() {
        Long authorId = entityManager.persistAndFlush(author1).getId();
        entityManager.persistAndFlush(author2);
        authorRepository.deleteById(authorId);
        List<Author> authorList = authorRepository.findAll();

        assertThat(authorList).size().isEqualTo(1);
        assertThat(authorList).contains(author2);
    }
}
