package org.example.SessionManagementSystem;

//Design a Session Management System that can create, validate, refresh, and
//invalidate user sessions securely after login.
//Each session must have a unique sessionId, expiry time, and be efficiently retrievable.

import java.util.*;
import java.io.*;

class User {
    String userId;
    String username;

    User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}

class Session {
    String sessionId;
    String userId;
    long expiryTime;

    Session(String sessionId, String userId, long expiryTime) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expiryTime = expiryTime;
    }
}

class SessionStore {
    private final Map<String, Session> store = new HashMap<>();

    void save(Session s) {
        store.put(s.sessionId, s);
    }

    Session get(String sessionId) {
        return store.get(sessionId);
    }

    void remove(String sessionId) {
        store.remove(sessionId);
    }
}

class SessionManager {
    private static final long TIMEOUT = 30 * 60 * 1000;
    private static SessionManager instance;
    private final SessionStore store = new SessionStore();

    private SessionManager() {
    }

    static SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    String createSession(User user) {
        String sid = UUID.randomUUID().toString();
        long expiry = System.currentTimeMillis() + TIMEOUT;
        store.save(new Session(sid, user.userId, expiry));
        return sid;
    }

    boolean validateSession(String sid) {
        Session s = store.get(sid);
        if (s == null) return false;
        if (System.currentTimeMillis() > s.expiryTime) {
            store.remove(sid);
            return false;
        }

        return true;
    }

    void refreshSession(String sid) {
        Session s = store.get(sid);
        if (s != null)
            s.expiryTime = System.currentTimeMillis() + TIMEOUT;
    }

    void invalidateSession(String sid) {
        store.remove(sid);
    }
}

class Main {
    public static void main(String[] args) throws Exception {
        PrintWriter out = new PrintWriter(System.out);

        User user = new User("101", "Bharadwaj");               // random input
        SessionManager sm = SessionManager.getInstance();

        String sessionId = sm.createSession(user);              // create session
        out.println("Session Created: " + sessionId);

        out.println("Valid: " + sm.validateSession(sessionId)); // validate

        sm.refreshSession(sessionId);                            // refresh
        out.println("Session Refreshed");

        sm.invalidateSession(sessionId);                         // logout
        out.println("Valid After Logout: " + sm.validateSession(sessionId));

        out.flush();
    }
}
