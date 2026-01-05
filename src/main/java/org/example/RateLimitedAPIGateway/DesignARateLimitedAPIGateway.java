package org.example.RateLimitedAPIGateway;

//Design an API Gateway that controls incoming requests from clients by enforcing rate limits.
//The system must allow or reject requests based on predefined limits per client within a time window.

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

interface RateLimiter {
    boolean allowRequest(String clientId);
}

class TokenBucket {
    private final int capacity;
    private int tokens;
    private final long refillIntervalMillis;
    private long lastRefillTimestamp;

    TokenBucket(int capacity, long refillIntervalMillis) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillIntervalMillis = refillIntervalMillis;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    synchronized boolean tryConsume() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTimestamp;

        int refillTokens = (int) (elapsed / refillIntervalMillis);

        if (refillTokens > 0) {
            tokens = Math.min(capacity, tokens + refillTokens);
            lastRefillTimestamp = now;
        }

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }
}

class TokenBucketRateLimiter implements RateLimiter {
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    private final int capacity;
    private final long refillIntervalMillis;

    TokenBucketRateLimiter(int capacity, long refillIntervalMillis) {
        this.capacity = capacity;
        this.refillIntervalMillis = refillIntervalMillis;
    }

    @Override
    public boolean allowRequest(String clientId) {
        buckets.putIfAbsent(clientId, new TokenBucket(capacity, refillIntervalMillis));

        return buckets.get(clientId).tryConsume();
    }
}

class ApiGateway {
    private final RateLimiter rateLimiter;

    ApiGateway(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    void handleRequest(String clientId) {
        if (rateLimiter.allowRequest(clientId)) {
            System.out.println("Request allowed for client: " + clientId);
        } else {
            System.out.println("Rate limit exceeded for client: " + clientId);
        }
    }
}

class Main {
    public static void main(String[] args) {

        // 5 requests per second
        RateLimiter limiter = new TokenBucketRateLimiter(5, 1000);

        ApiGateway gateway = new ApiGateway(limiter);

        // random input simulation
        String client = "client_123";

        for (int i = 1; i <= 10; i++) {
            gateway.handleRequest(client);
        }
    }
}