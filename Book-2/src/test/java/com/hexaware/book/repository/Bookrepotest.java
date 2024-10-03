package com.hexaware.book.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hexaware.book.entities.Book;
import com.hexaware.book.repositories.BookRepository;
import com.hexaware.book.service.BookServiceImpl;

@SpringBootTest
public class Bookrepotest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookService;

    @Test
    void saveBookTest() {
        // Given
        Book book = new Book(null, "1234567890", "Java Book", "Yashavant Kanetkar", 2022);

        // When
        Book savedBook = bookRepository.save(book);

     
    }

    @Test
    void findAllBooksTest() {
        // Given
        Book book1 = new Book(null, "1234567890", "Java Book", "Yashavant Kanetkar", 2022);
        Book book2 = new Book(null, "0987654321", "Another Book", "Author Name", 2023);
        bookRepository.save(book1);
        bookRepository.save(book2);

        // When
        List<Book> books = (List<Book>) bookRepository.findAll();

       
    }

    @Test
    void updateBookTitleTest() {
        // Given
        Book book = new Book(null, "1234567890", "Java Book", "Yashavant Kanetkar", 2022);
        Book savedBook = bookRepository.save(book);

        // When
        savedBook.setTitle("Updated Title");
        Book updatedBook = bookRepository.save(savedBook);

    }

    @Test
    void deleteBookTest() {
        // Given
        Book book = new Book(null, "1234567890", "Java Book", "Yashavant Kanetkar", 2022);
        Book savedBook = bookRepository.save(book);

        // When
        bookRepository.delete(savedBook);

    }
}