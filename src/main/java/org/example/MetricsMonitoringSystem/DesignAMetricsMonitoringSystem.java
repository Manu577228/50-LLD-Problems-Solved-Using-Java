package org.example.MetricsMonitoringSystem;

//Design a system that collects metrics (like CPU, memory, request count),
//stores them, and allows querying aggregated values (avg, min, max) over time.
//The system should be extensible, thread-safe, and suitable for real-time monitoring.

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Metric {
    String name;
    double value;
    long timestamp;

    Metric(String name, double value, long timestamp) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
    }
}

class MetricStore {
    Map<String, List<Metric>> store = new ConcurrentHashMap<>();

    void addMetric(Metric metric) {
        store.computeIfAbsent(metric.name, k -> Collections.synchronizedList(new ArrayList<>())).add(metric);
    }

    List<Metric> getMetrics(String name, long start, long end) {
        List<Metric> list = store.getOrDefault(name, Collections.emptyList());
        List<Metric> result = new ArrayList<>();

        for (Metric m : list) {
            if (m.timestamp >= start && m.timestamp <= end) {
                result.add(m);
            }
        }

        return result;
    }
}

interface AggregationStrategy {
    double aggregate(List<Metric> metrics);
}

class AverageAggregation implements AggregationStrategy {
    public double aggregate(List<Metric> metrics) {
        if (metrics.isEmpty()) return 0;

        double sum = 0;
        for (Metric m : metrics) {
            sum += m.value;
        }

        return sum / metrics.size();
    }
}

class MetricQueryService {
    MetricStore store;

    MetricQueryService(MetricStore store) {
        this.store = store;
    }

    double query(String name, long start, long end, AggregationStrategy strategy) {
        List<Metric> metrics = store.getMetrics(name, start, end);
        return strategy.aggregate(metrics);
    }
}

class MetricCollector {
    MetricStore store;

    MetricCollector(MetricStore store) {
        this.store = store;
    }

    void collect(String name, double value) {
        store.addMetric(new Metric(name, value, System.currentTimeMillis()));
    }
}

class Main {

    public static void main(String[] args) throws Exception {

        MetricStore store = new MetricStore();
        MetricCollector collector = new MetricCollector(store);
        MetricQueryService queryService = new MetricQueryService(store);

        collector.collect("cpu", 70);
        Thread.sleep(10);
        collector.collect("cpu", 80);
        Thread.sleep(10);
        collector.collect("cpu", 90);

        long now = System.currentTimeMillis();

        double avgCpu = queryService.query(
                "cpu",
                now - 1000,
                now,
                new AverageAggregation()
        );

        System.out.println("Average CPU Usage: " + avgCpu);
    }
}