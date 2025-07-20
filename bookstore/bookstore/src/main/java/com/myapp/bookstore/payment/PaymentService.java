package com.myapp.bookstore.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.annotation.PostConstruct; // Correct annotation for Jakarta

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void initializeStripe() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createPaymentIntent(int amount, String currency) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount); // Amount in the smallest currency unit (e.g., cents)
        params.put("currency", currency); // e.g., "usd"
        params.put("payment_method_types", List.of("card")); // Supported payment methods

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret(); // Return the client_secret
    }
}