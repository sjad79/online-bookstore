package com.myapp.bookstore.payment.records;

import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class BookSummary {

    private Long bookId;
    private String bookName;
    private int quantity;
    private BigDecimal price;

    public BookSummary() {}

    public BookSummary(Long bookId, String bookName, int quantity, BigDecimal price) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}