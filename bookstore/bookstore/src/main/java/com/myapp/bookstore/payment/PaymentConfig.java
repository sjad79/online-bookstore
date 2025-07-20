package com.myapp.bookstore.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.stripe.Stripe;

@Configuration
public class PaymentConfig {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Bean
    public boolean configureStripe() {
        // Set your Stripe API key from configuration file
        Stripe.apiKey = stripeApiKey;
        return true; // Indicate successful configuration
    }
}
