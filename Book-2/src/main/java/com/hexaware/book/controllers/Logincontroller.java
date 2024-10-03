package com.hexaware.book.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.book.dto.AuthRequest;
import com.hexaware.book.service.JwtService;

//
//@RestController
//@RequestMapping("/book/home")
//@CrossOrigin(origins = "http://localhost:3000")
//public class Logincontroller {
//
//
//    @Autowired
//    JwtService jwtService;
//    
//    @Autowired
//    private AuthenticationManager authManager;
//	
//	@PostMapping("/login")
//    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        if (authentication.isAuthenticated()) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            return jwtService.generateToken(authRequest.getUsername(), userDetails.getAuthorities());
//        } else {
//            throw new UsernameNotFoundException(authRequest.getUsername());
//        }
//    }
//	
//	}

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Logincontroller {

    @Autowired
    JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // If authentication is successful
            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Generate JWT token
                String jwtToken = jwtService.generateToken(authRequest.getUsername(), userDetails.getAuthorities());

                // Prepare response with token, role, and username
                Map<String, String> response = new HashMap<>();
                response.put("token", "Bearer " + jwtToken);

                // Extract role from authorities (assuming single role per user)
                String role = userDetails.getAuthorities().stream()
                                         .findFirst()
                                         .map(GrantedAuthority::getAuthority)
                                         .orElse("ROLE_USER");

                response.put("role", role);
                response.put("username", authRequest.getUsername());

                // Return OK response with the map
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // If authentication fails, throw an exception
                throw new UsernameNotFoundException("Invalid credentials for username: " + authRequest.getUsername());
            }
        } catch (Exception e) {
            // Return unauthorized status if authentication fails
            return new ResponseEntity<>(Map.of("message", "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }
    }
}
