package com.myapp.bookstore.payment.records;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "/api")
public class PaymentRecordController {
	
	@Autowired
	private final PaymentRecordService paymentRecordService;
	@Autowired
	private final PaymentRecordRepository paymentRecordRepository;


	public PaymentRecordController(PaymentRecordService paymentRecordService, PaymentRecordRepository paymentRecordRepository) {
		super();
		this.paymentRecordService = paymentRecordService;
		this.paymentRecordRepository = paymentRecordRepository;
	}
	

	@GetMapping("/admin/paymentrecords")
	public List<PaymentRecord> getAllPayments(){
		return paymentRecordService.getAllPayments();
	}
	
    @GetMapping("/user/paymentrecords/{userId}")
    public ResponseEntity<?> getPaymentsByUser(@PathVariable Long userId) {
        List<PaymentRecord> records = paymentRecordService.getRecordsByUserID(userId);

        if (records.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No payment records found for this user");
        }

        return ResponseEntity.ok(records);
    }

	
	@PostMapping
	public void savePayment(@RequestBody PaymentRecord record) {
		paymentRecordService.savePayment(record);
	}
	
	@DeleteMapping(path = "{recordId}")
	public void deletePayment(@PathVariable("recordId") Long recordId) {
		paymentRecordService.deletePayment(recordId);
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id) {
        Optional<PaymentRecord> paymentRecord = paymentRecordRepository.findById(id);

        if (paymentRecord.isPresent()) {
            return ResponseEntity.ok(paymentRecord.get()); // ✅ Return the found payment record
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment record not found"); // ✅ Handle missing payment record
        }
    }

	@PutMapping(path = "/admin/paymentrecords/delivered_status/{recordId}")
	public void checkPaymentDelivered(@PathVariable("recordId") Long recordId, @RequestBody Boolean status) {
		paymentRecordService.checkPaymentDelivered(recordId, status);
	}
	
	@PutMapping(path = "/user/paymentrecords/recieved_status/{recordId}")
	public void checkPaymentRecieved(@PathVariable("recordId") Long recordId, @RequestBody Boolean status) {
		paymentRecordService.checkPaymentRecieved(recordId, status);
	}
	

}
