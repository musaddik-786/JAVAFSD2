package com.hexaware.book.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.book.entities.Book;


public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
}
