package com.online.voting.election.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

//(Proper Microservice Way): Send JWT via Feign to Candidate Service for authentication/authorization
@Configuration
public class FeignConfig {

    //
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = getCurrentJwtToken();
            if (token != null) {
                requestTemplate.header("Authorization", token); // already has "Bearer " prefix
            }
        };
    }

    // Helper method to get the current JWT token from the request context
    private String getCurrentJwtToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null)
            return null;

        HttpServletRequest request = attributes.getRequest();
        return request.getHeader("Authorization"); // returns "Bearer <token>"
    }
}
