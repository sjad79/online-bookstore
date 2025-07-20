package com.myapp.bookstore.reports;

import com.myapp.bookstore.payment.records.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesReportRepository extends JpaRepository<PaymentRecord, Long> {
	
	@Query("""
		    SELECT new com.myapp.bookstore.reports.MonthlySalesDTO(
		        CAST(MONTH(p.dateOfPurchase) AS string),
		        SUM(b.quantity),
		        SUM(b.price * b.quantity)
		    )
		    FROM PaymentRecord p
		    JOIN p.books b
		    WHERE YEAR(p.dateOfPurchase) = :year
		    GROUP BY MONTH(p.dateOfPurchase)
		    ORDER BY MONTH(p.dateOfPurchase)
		""")
		List<MonthlySalesDTO> findMonthlySalesByYear(int year);

    @Query("""
        SELECT new com.myapp.bookstore.reports.TopBookDTO(
            b.bookId,
            b.bookName,
            SUM(b.quantity),
            SUM(CAST(b.quantity AS BigDecimal) * b.price)
        )
        FROM PaymentRecord p
        JOIN p.books b
        GROUP BY b.bookId, b.bookName
        ORDER BY SUM(b.quantity) DESC
    """)
    List<TopBookDTO> findBestSellingBooks();

    @Query("""
        SELECT new com.myapp.bookstore.reports.TopGenreDTO(
            g,
            COUNT(bSummary)
        )
        FROM PaymentRecord p
        JOIN p.books bSummary
        JOIN Book b ON bSummary.bookId = b.id
        JOIN b.genres g
        GROUP BY g
        ORDER BY COUNT(bSummary) DESC
    """)
    List<TopGenreDTO> findBestSellingGenres();
}