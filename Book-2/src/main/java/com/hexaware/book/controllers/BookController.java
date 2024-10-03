package com.hexaware.book.controllers;

import com.hexaware.book.Exception.ResourceNotFoundException;
import com.hexaware.book.dto.BookDTO;
import com.hexaware.book.service.BookService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController
@RequestMapping("/api/books")
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        try {
            BookDTO bookDTO = bookService.getBookByIsbn(isbn);
            return ResponseEntity.ok(bookDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public BookDTO addBook(@RequestBody @Valid BookDTO bookDTO) {
        return bookService.addBook(bookDTO);
    }

    @PutMapping("/update/{isbn}")
    public ResponseEntity<BookDTO> updateBook(@RequestBody @Valid BookDTO bookDTO, @PathVariable String isbn) {
        try {
            BookDTO updatedBookDTO = bookService.updateBook(bookDTO, isbn);
            return ResponseEntity.ok(updatedBookDTO);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        try {
            bookService.deleteBookByIsbn(isbn);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}