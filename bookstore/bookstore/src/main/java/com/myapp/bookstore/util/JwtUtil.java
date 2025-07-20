package com.myapp.bookstore.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {
    // Updated: Using a valid Base64-encoded secret key or a generated one
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("U29tZVNlY3JldEtleVRoYXRFbmNvZGVk".getBytes());

    // Generate a JWT token
    public String generateToken(String username, String role) {
    	long currentTime = System.currentTimeMillis();
    	System.out.println("Current Time: " + new Date(currentTime));
    	System.out.println("Token Expiry: " + new Date(currentTime + 1000 * 60 * 60 * 10));
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // ✅ Add roles to JWT
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10-hour expiry
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }


    // Extract the username from the token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Updated parserBuilder
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
