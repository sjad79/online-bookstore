package com.myapp.bookstore.config;

import com.myapp.bookstore.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.myapp.bookstore.service.MyUserDetailsService;
import com.myapp.bookstore.util.JwtUtil;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(MyUserDetailsService userDetailsService, JwtUtil jwtUtil, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // 1. Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 3. Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 4. Security Filter Chain with JWT Authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults()) // ✅ Enable CORS
            .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF for JWT-based authentication
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ Ensure stateless authentication
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**", "/api/public/**").permitAll() // ✅ Public paths that don't require authentication
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // ✅ Protect admin-only paths
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN") // ✅ Protect admin-only paths
                .anyRequest().authenticated() // ✅ Protect all other endpoints
            )
            .authenticationProvider(authenticationProvider()) // ✅ Set the authentication provider
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // ✅ Apply JWT filter at correct position

        return http.build();
    }

    // 5. CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // ✅ Allow React frontend
        configuration.addAllowedMethod("*"); // ✅ Allow all HTTP methods
        configuration.addAllowedHeader("*"); // ✅ Allow all headers
        configuration.setAllowCredentials(true); // ✅ Enables cookie-based authentication

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // ✅ Apply to all endpoints
        return source;
    }
}