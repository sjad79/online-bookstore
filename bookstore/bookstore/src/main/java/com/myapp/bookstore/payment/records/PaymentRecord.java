package com.myapp.bookstore.payment.records;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Payment_Records")
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;
    private String username;
    
    @ElementCollection
    @CollectionTable(
        name = "payment_record_books",
        joinColumns = @JoinColumn(name = "payment_record_id")
    )
    private List<BookSummary> books;

    private BigDecimal totalPrice;
    private LocalDate dateOfPurchase;
    private Boolean delivered = false;
    private Boolean received = false;

    public PaymentRecord() {}

    public PaymentRecord(Long userId, String username, List<BookSummary> books, BigDecimal totalPrice,
                         LocalDate dateOfPurchase, Boolean delivered, Boolean received) {
        this.userId = userId;
        this.username = username;
        this.books = books;
        this.totalPrice = totalPrice;
        this.dateOfPurchase = dateOfPurchase;
        this.delivered = delivered;
        this.received = received;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<BookSummary> getBooks() { return books; }
    public void setBooks(List<BookSummary> books) { this.books = books; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public LocalDate getDateOfPurchase() { return dateOfPurchase; }
    public void setDateOfPurchase(LocalDate dateOfPurchase) { this.dateOfPurchase = dateOfPurchase; }

    public Boolean getDelivered() { return delivered; }
    public void setDelivered(Boolean delivered) { this.delivered = delivered; }

    public Boolean getReceived() { return received; }
    public void setReceived(Boolean received) { this.received = received; }
}