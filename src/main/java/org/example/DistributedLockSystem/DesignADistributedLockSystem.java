package org.example.DistributedLockSystem;

//Design a distributed lock system that allows multiple services or nodes
//to mutually exclusively access shared resources.
//The lock must work correctly even when multiple machines, threads, or processes compete concurrently.
//The system should be safe, fault-tolerant, and scalable.

import java.util.*;
import java.util.concurrent.*;

class Lock {
    String resourceId;
    String ownerId;
    long expiryTime;

    Lock(String r, String o, long e) {
        this.resourceId = r;
        this.ownerId = o;
        this.expiryTime = e;
    }
}

interface LockStore {
    boolean acquire(String resourceId, String ownerId, long ttl);

    boolean release(String resourceId, String ownerId);

    boolean isLocked(String resourceId);
}

class InMemoryLockStore implements LockStore {
    private final ConcurrentHashMap<String, Lock> store = new ConcurrentHashMap<>();

    public synchronized boolean acquire(String resourceId, String ownerId, long ttl) {
        long now = System.currentTimeMillis();
        Lock existing = store.get(resourceId);

        if (existing == null || existing.expiryTime < now) {
            store.put(resourceId, new Lock(resourceId, ownerId, now + ttl));
            return true;
        }

        return false;
    }

    public synchronized boolean release(String resourceId, String ownerId) {
        Lock lock = store.get(resourceId);

        if (lock != null && lock.ownerId.equals(ownerId)) {
            store.remove(resourceId);
            return true;
        }

        return false;
    }

    public boolean isLocked(String resourceId) {
        Lock lock = store.get(resourceId);
        return lock != null && lock.expiryTime >= System.currentTimeMillis();
    }
}

class DistributedLockManager {
    private final LockStore store;

    DistributedLockManager(LockStore s) {
        this.store = s;
    }

    boolean lock(String resourceId, String ownerId, long ttl) {
        return store.acquire(resourceId, ownerId, ttl);
    }

    boolean unlock(String resourceId, String ownerId) {
        return store.release(resourceId, ownerId);
    }
}

class Main {

    public static void main(String[] args) {

        LockStore store = new InMemoryLockStore();           // lock store
        DistributedLockManager manager = new DistributedLockManager(store);

        String resource = "ORDER_101";                       // random resource
        String clientA = "CLIENT_A";                         // random client
        String clientB = "CLIENT_B";

        System.out.println(manager.lock(resource, clientA, 5000)); // true
        System.out.println(manager.lock(resource, clientB, 5000)); // false
        System.out.println(manager.unlock(resource, clientA));     // true
        System.out.println(manager.lock(resource, clientB, 5000)); // true
    }
}

