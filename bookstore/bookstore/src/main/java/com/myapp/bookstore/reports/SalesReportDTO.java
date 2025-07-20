package com.myapp.bookstore.reports;

import java.math.BigDecimal;
import java.util.List;

public class SalesReportDTO {
	
    private List<TopBookDTO> topBooks;
    private List<TopGenreDTO> topGenres;

    // Constructor
    public SalesReportDTO(List<TopBookDTO> topBooks, List<TopGenreDTO> topGenres) {

        this.topBooks = topBooks;
        this.topGenres = topGenres;
    }

    // Getters & Setters

    public List<TopBookDTO> getTopBooks() { return topBooks; }
    public void setTopBooks(List<TopBookDTO> topBooks) { this.topBooks = topBooks; }

    public List<TopGenreDTO> getTopGenres() { return topGenres; }
    public void setTopGenres(List<TopGenreDTO> topGenres) { this.topGenres = topGenres; }
    
}