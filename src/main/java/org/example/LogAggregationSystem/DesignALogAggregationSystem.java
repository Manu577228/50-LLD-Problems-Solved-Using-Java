package org.example.LogAggregationSystem;

//Design a system that collects logs from multiple services, stores them centrally,
//and allows querying/searching
//logs efficiently based on filters like service name, level, and time range.

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

enum LogLevel {
    INFO, WARN, ERROR, DEBUG
}

class LogEntry {
    String serviceName;
    LogLevel level;
    String message;
    long timestamp;

    LogEntry(String serviceName, LogLevel level, String message) {
        this.serviceName = serviceName;
        this.level = level;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}

interface ILogStore {
    void store(LogEntry log);

    List<LogEntry> getAll();
}

class InMemoryLogStore implements ILogStore {
    private final List<LogEntry> logs = new CopyOnWriteArrayList<>();

    public void store(LogEntry log) {
        logs.add(log);
    }

    public List<LogEntry> getAll() {
        return logs;
    }
}

class LogCollector {
    private final ILogStore store;

    LogCollector(ILogStore store) {
        this.store = store;
    }

    void collect(LogEntry log) {
        store.store(log);
    }
}

class LogQueryService {
    private final ILogStore store;

    LogQueryService(ILogStore store) {
        this.store = store;
    }

    List<LogEntry> query(String service, LogLevel level) {
        List<LogEntry> result = new ArrayList<>();

        for (LogEntry log : store.getAll()) {
            if (log.serviceName.equals(service) && log.level == level) {
                result.add(log);
            }
        }

        return result;
    }
}

class LogSource {
    private final String serviceName;
    private final LogCollector collector;

    LogSource(String serviceName, LogCollector collector) {
        this.serviceName = serviceName;
        this.collector = collector;
    }

    void log(LogLevel level, String message) {
        collector.collect(new LogEntry(serviceName, level, message));
    }
}

class Main {

    public static void main(String[] args) {

        // Create shared log store
        ILogStore store = new InMemoryLogStore();

        // Create collector
        LogCollector collector = new LogCollector(store);

        // Create services
        LogSource authService = new LogSource("AuthService", collector);
        LogSource paymentService = new LogSource("PaymentService", collector);

        // Random input logs
        authService.log(LogLevel.INFO, "User logged in");
        authService.log(LogLevel.ERROR, "Invalid token");
        paymentService.log(LogLevel.INFO, "Payment initiated");
        paymentService.log(LogLevel.ERROR, "Payment failed");

        // Query logs
        LogQueryService queryService = new LogQueryService(store);
        List<LogEntry> errors = queryService.query("AuthService", LogLevel.ERROR);
        List<LogEntry> paymentErrors = queryService.query("PaymentService", LogLevel.INFO);


        // Print queried logs
        for (LogEntry log : errors) {
            System.out.println(log.serviceName + " | " + log.level + " | " + log.message);
        }

        for (LogEntry log : paymentErrors) {
            System.out.println(log.serviceName + " | " + log.level + " | " + log.message);
        }
    }
}
