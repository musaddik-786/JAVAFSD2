package com.hexaware.book.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hexaware.book.dto.UserDTO;
import com.hexaware.book.service.UserServiceImpl;

//@RestController
//@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")
//public class UserController {
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @PostMapping
//    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) {
//        try {
//            userService.addUser(userDTO);
//            return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        }
//    }
//
//    // Additional endpoints for user management (e.g., update, delete) can be added here
//}

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<Map<String, String>> addUser(@RequestBody UserDTO userDTO) {
        try {
            userService.addUser(userDTO);
            // Create a response map
            Map<String, String> response = new HashMap<>();
            response.put("message", "User added successfully");
            response.put("username", userDTO.getUsername()); // Assuming UserDTO has a getUsername method

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // Return an error message in JSON format
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
    }
}


