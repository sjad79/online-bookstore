package com.myapp.bookstore.payment.records;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
	Optional<PaymentRecord> findById(Long id);
	List<PaymentRecord> findByUserId(Long userId);
}