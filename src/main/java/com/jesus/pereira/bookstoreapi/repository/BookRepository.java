package com.jesus.pereira.bookstoreapi.repository;

import com.jesus.pereira.bookstoreapi.domain.Author;
import com.jesus.pereira.bookstoreapi.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByNameContainingIgnoreCase(@Param("name") String name);

    Optional<Book> findByNameIgnoreCase(@Param("name") String name);

    List<Book> findByAuthor(Author author);

}
