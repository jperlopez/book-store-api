package com.jesus.pereira.bookstoreapi.repository;

import com.jesus.pereira.bookstoreapi.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
