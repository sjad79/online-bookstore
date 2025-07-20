package com.myapp.bookstore.reports;

import java.math.BigDecimal;

public class MonthlySalesDTO {

    private String month;             // e.g., "January", "Feb", or "03"
    private Long totalBooksSold;       // Total number of books sold in the month
    private BigDecimal totalRevenue; // Total revenue generated for the month

    public MonthlySalesDTO() {}

    public MonthlySalesDTO(String month, Long totalBooksSold, BigDecimal totalRevenue) {
        this.month = month;
        this.totalBooksSold = totalBooksSold;
        this.totalRevenue = totalRevenue;
    }

    // Getters and setters
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Long getTotalBooksSold() { return totalBooksSold; }
    public void setTotalBooksSold(Long totalBooksSold) { this.totalBooksSold = totalBooksSold; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}