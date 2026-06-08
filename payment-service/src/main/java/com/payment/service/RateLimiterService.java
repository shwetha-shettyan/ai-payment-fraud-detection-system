package com.payment.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(3, Refill.greedy(3, Duration.ofSeconds(60))))
                .build();
    }

    public Bucket resolveBucket(String userId) {
        return buckets.computeIfAbsent(userId, k -> createNewBucket());
    }
}

