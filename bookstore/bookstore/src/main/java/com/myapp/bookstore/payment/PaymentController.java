package com.myapp.bookstore.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.bookstore.payment.records.BookSummary;
import com.myapp.bookstore.payment.records.PaymentRecord;
import com.myapp.bookstore.payment.records.PaymentRecordRepository;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/payments")
public class PaymentController {
    @Autowired
    private PaymentRecordRepository paymentRecordRepository;

    // Inject the webhook signing secret from application.properties
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/create")
    public String createPaymentIntent(@RequestBody Map<String, Object> requestBody) throws StripeException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // ✅ Extract metadata from request body safely
        Map<String, String> metadata = (Map<String, String>) requestBody.get("metadata");
        String userId = metadata.get("userId");
        String username = metadata.get("username");

        // ✅ Convert books list to a compact format for Stripe metadata
        List<Map<String, Object>> books = (List<Map<String, Object>>) requestBody.get("books");

     // ✅ Ensure books list is not null
	     if (books == null) {
	         books = new ArrayList<>(); // ✅ Default to empty list if null
	     }
	     
	     System.out.println("📦 Incoming books from client: " + books);
	
	     String booksJson = "[]"; // Default value in case of error
	     try {
	         booksJson = objectMapper.writeValueAsString(
	             books.stream().map(book -> Map.of(
	                 "id", book.get("id"),
	                 "name", book.get("name"),
	                 "quantity", book.get("quantity"),
	                 "price", book.get("price")
	             )).toList()
	         );
	     } catch (JsonProcessingException e) {
	         System.err.println("❌ Error serializing books metadata: " + e.getMessage());
	     }

        System.out.println("Metadata received in backend:");
        System.out.println("User ID: " + userId);
        System.out.println("Username: " + username);
        System.out.println("Books JSON: " + booksJson);
        
     // 🛡 Safe and flexible amount handling
        Object rawAmount = requestBody.get("amount");
        long amountInCents = 0L;
        if (rawAmount instanceof Number) {
            amountInCents = ((Number) rawAmount).longValue();
        } else {
            throw new IllegalArgumentException("Invalid amount value: " + rawAmount);
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
        	.setAmount(amountInCents)
            .setCurrency((String) requestBody.get("currency"))
            .putMetadata("userId", userId != null ? userId : "unknown")
            .putMetadata("username", username != null ? username : "unknown")
            .putMetadata("books", booksJson.length() <= 500 ? booksJson : "[]") // ✅ Prevent exceeding Stripe’s limit
            .build();

        return PaymentIntent.create(params).getClientSecret(); // ✅ Returns client secret for Stripe checkout
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Log the payload and signature for debugging
            System.out.println("Received Payload: " + payload);
            System.out.println("Stripe-Signature: " + sigHeader);

            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            System.out.println("Webhook Event Type: " + event.getType());
            System.out.println("Webhook Raw JSON Object: " + event.getDataObjectDeserializer().getRawJson());

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    try {
                        // ✅ Extract metadata manually using ObjectMapper
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.registerModule(new JavaTimeModule());

                        JsonNode rawJson = objectMapper.readTree(event.getDataObjectDeserializer().getRawJson());

                        // ✅ Extract metadata safely
                        JsonNode metadataNode = rawJson.get("metadata");

                        if (metadataNode != null) {
                            String userId = metadataNode.has("userId") ? metadataNode.get("userId").asText() : "unknown";
                            String username = metadataNode.has("username") ? metadataNode.get("username").asText() : "unknown";
                            String booksJsonString = metadataNode.has("books") ? metadataNode.get("books").asText() : "[]"; // ✅ Ensure books metadata is treated as a string

                            // ✅ Convert books JSON string to JSON node
                            JsonNode booksJsonNode = objectMapper.readTree(booksJsonString);
                            System.out.println("📦 Raw books metadata string: " + booksJsonString);
                            System.out.println("📘 Parsed booksJsonNode: " + booksJsonNode);
                            List<BookSummary> processedBooks = new ArrayList<>();

                            for (JsonNode bookNode : booksJsonNode) {
                                Long bookId = bookNode.has("id") ? bookNode.get("id").asLong() : -1L;
                                String bookName = bookNode.has("name") ? bookNode.get("name").asText() : "Unknown";
                                int quantity = bookNode.has("quantity") ? bookNode.get("quantity").asInt() : 0;
                                BigDecimal price = bookNode.has("price") ? new BigDecimal(bookNode.get("price").asText()) : BigDecimal.ZERO;

                                processedBooks.add(new BookSummary(bookId, bookName, quantity, price)); // ✅ Store price too
                            }

                            // ✅ Save payment record in the database
                            PaymentRecord payment = new PaymentRecord();
                            payment.setUserId(Long.parseLong(userId));
                            payment.setUsername(username);
                            payment.setBooks(processedBooks); // ✅ Store structured book details
                            payment.setTotalPrice(BigDecimal.valueOf(rawJson.get("amount").asDouble() / 100.0));
                            payment.setDateOfPurchase(LocalDate.now());
                            payment.setDelivered(false);
                            payment.setReceived(false);
                            
                            System.out.println("📚 Books being saved: " + processedBooks.size());

                            paymentRecordRepository.save(payment);

                            System.out.println("✅ Payment record saved for user: " + username);
                        } else {
                            System.err.println("❌ Metadata field is missing in Stripe webhook payload!");
                        }
                    } catch (Exception e) {
                        System.err.println("❌ Error extracting metadata: " + e.getMessage());
                    }
                    break;

                case "payment_intent.payment_failed":
                    System.out.println("Payment failed.");
                    break;

                default:
                    System.out.println("Unhandled event type: " + event.getType());
                    return ResponseEntity.ok("Event type not supported.");
            }

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (SignatureVerificationException e) {
            System.err.println("Invalid signature: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid signature.");
        } catch (Exception e) {
            System.err.println("Error processing webhook: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook processing error.");
        }
    }
}