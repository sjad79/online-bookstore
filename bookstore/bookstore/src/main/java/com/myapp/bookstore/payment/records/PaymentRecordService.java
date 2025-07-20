package com.myapp.bookstore.payment.records;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentRecordService {
	
	@Autowired
	private final PaymentRecordRepository paymentRecordRepository;
	
	
	
	public PaymentRecordService(PaymentRecordRepository paymentRecordRepository) {
		super();
		this.paymentRecordRepository = paymentRecordRepository;
	}


	public void savePayment(PaymentRecord record) {
	    paymentRecordRepository.save(record); // ✅ Saves record
	}

	public Optional<PaymentRecord> getPaymentById(Long id) {
	    return paymentRecordRepository.findById(id); // ✅ Retrieves payment by ID
	}

	public List<PaymentRecord> getAllPayments() {
	    return paymentRecordRepository.findAll(); // ✅ Fetches all payment records
	}

	public void deletePayment(Long id) {
	    paymentRecordRepository.deleteById(id); // ✅ Deletes payment record by ID
	}


	public void checkPaymentDelivered(Long recordId, Boolean status) {
		// TODO Auto-generated method stub
		Optional<PaymentRecord> recordOptional = paymentRecordRepository.findById(recordId);
		
		if(recordOptional.isEmpty()) {
			throw new IllegalStateException("Book with id " + recordId + " does not exist");
		}
		
		PaymentRecord record = recordOptional.get();
		record.setDelivered(status);
		
		paymentRecordRepository.save(record);
		System.out.println("Payment record updated: " + record.getId());
		
	}


	public void checkPaymentRecieved(Long recordId, Boolean status) {
		// TODO Auto-generated method stub
		Optional<PaymentRecord> recordOptional = paymentRecordRepository.findById(recordId);
		
		if(recordOptional.isEmpty()) {
			throw new IllegalStateException("Book with id " + recordId + " does not exist");
		}
		
		PaymentRecord record = recordOptional.get();
		record.setReceived(status);
		
		paymentRecordRepository.save(record);
		System.out.println("Payment record updated: " + record.getId());
		
	}


    public List<PaymentRecord> getRecordsByUserID(Long userId) {
        return paymentRecordRepository.findByUserId(userId); // ✅ Calls repository method
    }

	
}
