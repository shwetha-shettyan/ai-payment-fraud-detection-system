package com.payment.security;

import com.payment.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RateLimitFilter implements Filter {

    private final RateLimiterService rateLimiterService;

    public RateLimitFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().contains("/payments")) {
            // Extract userId (from JWT or header for now)
            String userId = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();

            if (userId == null) {
                httpResponse.setStatus(400);
                httpResponse.getWriter().write("Missing userId");
                return;
            }

            Bucket bucket = rateLimiterService.resolveBucket(userId);

            if (bucket.tryConsume(1)) {
                chain.doFilter(request, response);
            } else {
                httpResponse.setStatus(429);
                httpResponse.getWriter().write("Too many requests");
            }
        } else {
            chain.doFilter(request, response);
            return;
        }
    }
}