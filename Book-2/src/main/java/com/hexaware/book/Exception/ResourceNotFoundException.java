package com.hexaware.book.Exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource) {
        super("Resource not found: " + resource);
    }
}
