package org.example.RedisLikeCache;

//Design an in-memory key-value cache system similar to Redis that supports fast read/write operations.
//The cache should support TTL (expiry) and an eviction policy (LRU) when capacity is reached.

import java.io.*;
import java.util.*;

class CacheEntry {
    String value;
    long expiryTime;

    CacheEntry(String v, long ttl) {
        value = v;
        expiryTime = ttl <= 0 ? Long.MAX_VALUE : System.currentTimeMillis() + ttl;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

interface EvictionPolicy {
    void onGet(String key);

    void onPut(String key);

    String evict();
}

class Node {
    String key;
    Node prev, next;

    Node(String k) {
        key = k;
    }
}

class LRUEvictionPolicy implements EvictionPolicy {
    Map<String, Node> map = new HashMap<>();
    Node head = new Node(null);
    Node tail = new Node(null);

    LRUEvictionPolicy() {
        head.next = tail;
        tail.prev = head;
    }

    private void remove(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }

    private void addFront(Node n) {
        n.next = head.next;
        n.prev = head;
        head.next.prev = n;
        head.next = n;
    }

    public void onGet(String key) {
        if (!map.containsKey(key)) return;
        Node n = map.get(key);
        remove(n);
        addFront(n);
    }

    public void onPut(String key) {
        if (map.containsKey(key)) {
            remove(map.get(key));
        }
        Node n = new Node(key);
        map.put(key, n);
        addFront(n);
    }

    public String evict() {
        Node lru = tail.prev;
        if (lru == head) return null;
        map.remove(lru.key);
        return lru.key;
    }
}

class Cache {
    int capacity;
    Map<String, CacheEntry> store;
    EvictionPolicy policy;

    Cache(int cap) {
        capacity = cap;
        store = new HashMap<>();
        policy = new LRUEvictionPolicy();
    }

    String get(String key) {
        if (!store.containsKey(key)) return null;

        CacheEntry e = store.get(key);

        if (e.isExpired()) {
            store.remove(key);
            return null;
        }

        policy.onGet(key);
        return e.value;
    }

    void put(String key, String value, long ttl) {
        if (store.size() >= capacity) {
            String evicted = policy.evict();
            if (evicted != null) store.remove(evicted);
        }

        store.put(key, new CacheEntry(value, ttl));
        policy.onPut(key);
    }
}

class Main {
    public static void main(String[] args) {

        Cache cache = new Cache(2);

        cache.put("A", "Apple", 5000);
        cache.put("B", "Banana", 5000);

        System.out.println(cache.get("A")); // Apple

        cache.put("C", "Cherry", 5000);     // Evicts B

        System.out.println(cache.get("B")); // null
        System.out.println(cache.get("C")); // Cherry
    }
}

