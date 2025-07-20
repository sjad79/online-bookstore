package com.myapp.bookstore.dto;

public class AuthResponse {
    private String token;

    // Constructor
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter (optional, only if you need it)
    public void setToken(String token) {
        this.token = token;
    }
}
