package com.jesus.pereira.bookstoreapi.repository;

import com.jesus.pereira.bookstoreapi.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

     List<Author> findByNameContainingIgnoreCase(@Param("name") String name);

     Optional<Author> findByNameIgnoreCase(@Param("name") String name);

     Optional<Author> findByNameAndSurnameIgnoreCase(@Param("name") String name, @Param("surname") String surname);
}
