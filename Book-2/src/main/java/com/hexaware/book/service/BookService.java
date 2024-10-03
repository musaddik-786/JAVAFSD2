package com.hexaware.book.service;

import java.util.List;
import java.util.Optional;

import com.hexaware.book.Exception.ResourceNotFoundException;
import com.hexaware.book.dto.BookDTO;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookByIsbn(String isbn) throws ResourceNotFoundException;
    BookDTO addBook(BookDTO bookDTO);
    BookDTO updateBook(BookDTO bookDTO, String isbn) throws ResourceNotFoundException;
    void deleteBookByIsbn(String isbn) throws ResourceNotFoundException;
}
