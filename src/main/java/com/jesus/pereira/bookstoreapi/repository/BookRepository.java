package com.jesus.pereira.bookstoreapi.repository;

import com.jesus.pereira.bookstoreapi.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
