package org.example.KeyValueStore;

//Design an in-memory key-value store that allows storing, retrieving,
//updating, and deleting key-value pairs efficiently.
//The system should support basic CRUD operations with fast access and
//should run entirely in memory.

import java.io.*;
import java.util.*;

class StorageEngine {
    private final Map<String, String> map;

    StorageEngine() {
        this.map = new HashMap<>();
    }

    void put(String key, String value) {
        map.put(key, value);
    }

    String get(String key) {
        return map.get(key);
    }

    void delete(String key) {
        map.remove(key);
    }

    boolean contains(String key) {
        return map.containsKey(key);
    }
}

class KeyValueStore {
    private final StorageEngine engine;

    KeyValueStore() {
        this.engine = new StorageEngine();
    }

    void put(String key, String value) {
        engine.put(key, value);
    }

    String get(String key) {
        return engine.get(key);
    }

    void delete(String key) {
        engine.delete(key);
    }

    boolean contains(String key) {
        return engine.contains(key);
    }
}

class Main {

    public static void main(String[] args) throws Exception {

        // Fast I/O setup
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        // Initialize Key-Value Store
        KeyValueStore store = new KeyValueStore();

        // Random inputs
        store.put("user1", "Bharadwaj1");
        store.put("user2", "Bharadwaj2");

        out.println(store.get("user1"));   // Bharadwaj1
        out.println(store.contains("user2")); // true

        store.delete("user2");

        out.println(store.contains("user2")); // false

        out.flush();
    }
}



