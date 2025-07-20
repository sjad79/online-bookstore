package com.myapp.bookstore.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.myapp.bookstore.book.Book;
import com.myapp.bookstore.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "cart_books",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>(); // Ensure books is always initialized

    private BigDecimal totalPrice;

    public Cart() {
        super();
        this.books = new ArrayList<>(); // Initialize books here as well to cover default constructor
    }

    public Cart(User user, List<Book> books, BigDecimal totalPrice) {
        super();
        this.user = user;
        this.books = books != null ? books : new ArrayList<>(); // Handle null books gracefully
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books != null ? books : new ArrayList<>(); // Ensure books is never null
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Additional getters and setters (if needed)
}