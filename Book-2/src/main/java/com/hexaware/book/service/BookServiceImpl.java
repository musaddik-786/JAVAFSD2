package com.hexaware.book.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.hexaware.book.Exception.ResourceNotFoundException;
import com.hexaware.book.dto.BookDTO;
import com.hexaware.book.entities.Book;
import com.hexaware.book.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(book -> new BookDTO(
            book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublicationYear())
        ).collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookByIsbn(String isbn) throws ResourceNotFoundException {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        return new BookDTO(book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPublicationYear());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = new Book(null, bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublicationYear());
        Book savedBook = bookRepository.save(book);
        return new BookDTO(savedBook.getTitle(), savedBook.getAuthor(), savedBook.getIsbn(), savedBook.getPublicationYear());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public BookDTO updateBook(BookDTO bookDTO, String isbn) throws ResourceNotFoundException {
        Book existingBook = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        Book updatedBook = bookRepository.save(existingBook);
        return new BookDTO(updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getIsbn(), updatedBook.getPublicationYear());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteBookByIsbn(String isbn) throws ResourceNotFoundException {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }
}
