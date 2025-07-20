package com.myapp.bookstore.reports;

public class TopGenreDTO {
	
    private String genreName;
    private Long totalSales;

    public TopGenreDTO(String genreName, Long totalSales) {
        this.genreName = genreName;
        this.totalSales = totalSales;
    }

    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }

    public Long getTotalSales() { return totalSales; }
    public void setTotalSales(Long totalSales) { this.totalSales = totalSales; }
    
}