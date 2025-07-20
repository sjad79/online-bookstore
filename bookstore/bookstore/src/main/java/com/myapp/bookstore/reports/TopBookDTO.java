package com.myapp.bookstore.reports;

import java.math.BigDecimal;

public class TopBookDTO {
	
    private Long bookId;
    private String bookName;
    private Long sales;
    private BigDecimal revenue;

    public TopBookDTO(Long bookId, String bookName, Long sales, BigDecimal revenue) {
    	
    	this.bookId = bookId;
        this.bookName = bookName;
        this.sales = sales;
        this.revenue = revenue;
    }
    

    public Long getBookId() {
		return bookId;
	}


	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}


	public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public Long getSales() { return sales; }
    public void setSales(Long sales) { this.sales = sales; }

    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
    
}


