package com.hexaware.book.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    
    // Secret key is hardcoded for simplicity; consider using environment variables or secure storage for production
    private static final String SECRET = "HnEDCKYobu5um1XAsbqn4rkC+DWXzT4Sfx2B+NT5Q437fvgg4rVjFoMLht0bFR2E1AfcvqEIJ4KrsqIxLSCcQgZ16qOlT6Io7E5tsqYXHaRs8DtjFRWOBsMAU4CJctSzRiXKEEDTx9Z86/j/BecaMG4CQw+6K3+RKkITRzIUVhATOrM5rZOpIGYWH7/f9WHxvmCbDBdqVz0bW7+tg7ggGd1ysDL7BrJ84Rj+qxMgabCukXP+77NkKCuifw7i1WknGTSmixHPFL46hs3XoJ7M/p9b0IkqfSRmBfnA9pDyqeDF00Rom+xlsiUtT/tC7UD0tHi2HeztEeTVbmu35HXvLZ9RoyztXy7uUdq1IS2N6ao=";

    // Generate a token with claims and username
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        String role = authorities.iterator().next().getAuthority();
        claims.put("role", role);
        return createToken(claims, username);
    }

    // Create a JWT token with claims, username, and expiration
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get the signing key from the secret
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract claims from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token by checking username and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
