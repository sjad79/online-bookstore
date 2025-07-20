package com.myapp.bookstore.auth;

import com.myapp.bookstore.util.JwtUtil;
import com.myapp.bookstore.user.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.myapp.bookstore.book.Book;
import com.myapp.bookstore.cart.*;
import com.myapp.bookstore.dto.AuthRequest;
import com.myapp.bookstore.dto.AuthResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest authRequest, 
            HttpServletResponse response) {
        try {
            User user = userService.getUserByName(authRequest.getUsername()); 
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid username or password");
            }

            // ✅ Completely removed cart-related logic

            String role = user.getRole(); // ✅ Fetch single role (e.g., "ROLE_ADMIN" or "ROLE_USER")
            String token = jwtUtil.generateToken(user.getName(), role);

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60 * 24);

            response.addCookie(jwtCookie);
            
            Map<String, Object> responseBody = Map.of(
                    "message", "Login successful",
                    "token", token,
                    "userId", user.getId()
                );

            System.out.println("Login Response: " + responseBody);

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }

    // ✅ Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User newUser,
            @CookieValue(value = "cart", defaultValue = "") String cartCookie,
            HttpServletResponse response) {
        try {
            User savedUser = userService.addNewUser(newUser);

            Cart userCart = new Cart();
            userCart.setUser(savedUser);
            
            System.out.println("Cart cookie content: " + cartCookie);

            List<Book> booksFromCookie = cartService.getCartItemsFromCookie(cartCookie);
            System.out.println("Books extracted from cookie: " + booksFromCookie);

            if (booksFromCookie != null && !booksFromCookie.isEmpty()) {
                userCart.setBooks(booksFromCookie);
            }

            cartService.saveUserCart(userCart);

            Cookie clearedCookie = new Cookie("cart", "");
            clearedCookie.setMaxAge(0);
            response.addCookie(clearedCookie);
            
            String role = savedUser.getRole(); // ✅ Fetch single role (e.g., "ROLE_ADMIN" or "ROLE_USER")
            String token = jwtUtil.generateToken(savedUser.getName(), role);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();

        // ✅ Extract JWT from cookies
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // ✅ Handle missing or invalid token
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false, "message", "No token found"));
        }

        String username = jwtUtil.extractUsername(token);

        // ✅ Ensure token is valid before proceeding
        if (!jwtUtil.validateToken(token, username)) {
            return ResponseEntity.status(403).body(Map.of("authenticated", false, "message", "Invalid or expired token"));
        }

        // ✅ Retrieve user from database
        User user = userService.getUserByName(username);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false, "message", "User not found"));
        }

        return ResponseEntity.ok(Map.of(
            "authenticated", true,
            "username", username,
            "userId", user.getId() // ✅ Include userId for better session tracking
        ));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // ✅ Create an expired JWT cookie to overwrite existing one
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Keep secure in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Expire the cookie immediately

        response.addCookie(jwtCookie); // ✅ Send expired cookie to clear JWT
        
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}