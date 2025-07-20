package com.myapp.bookstore.reports;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sales-report")
public class SalesReportController {

    private final SalesReportService salesReportService;

    public SalesReportController(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

	/*
	 * @GetMapping("/{month}") public SalesReportDTO
	 * getMonthlySalesReport(@PathVariable String month) { return
	 * salesReportService.generateMonthlyReport(month); }
	 */
    @GetMapping("/monthlysales/{year}")
    public List<MonthlySalesDTO> getMonthlySalesReport(@PathVariable String year) {
        int parsedYear = Integer.parseInt(year);
        return salesReportService.generateMonthlySalesReport(parsedYear);
    }
    
    @GetMapping("/topSales")
    public SalesReportDTO getTopSalesReport() {
        return salesReportService.generateTopSalesReport();
    }
    
    
    
}
