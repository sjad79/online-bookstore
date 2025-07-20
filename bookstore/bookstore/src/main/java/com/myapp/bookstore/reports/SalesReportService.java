package com.myapp.bookstore.reports;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SalesReportService {

    private final SalesReportRepository salesReportRepository;

    public SalesReportService(SalesReportRepository salesReportRepository) {
        this.salesReportRepository = salesReportRepository;
    }

    public List<MonthlySalesDTO> generateMonthlySalesReport(int year) {
        return salesReportRepository.findMonthlySalesByYear(year);
    }
    
    public SalesReportDTO generateTopSalesReport() {
    	List<TopBookDTO> topBooks = salesReportRepository.findBestSellingBooks();
    	List<TopGenreDTO> topGenres = salesReportRepository.findBestSellingGenres();
    	SalesReportDTO salesReport = new SalesReportDTO(topBooks, topGenres);
    	
        return salesReport;
    }
}