package org.example.TaskScheduler;

//Design a Task Scheduler that allows clients to submit tasks with a scheduled execution time.
//The system should execute tasks at or after their scheduled time, reliably and efficiently.
//Tasks can be one-time and must be executed exactly once.

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

class Task implements Comparable<Task> {
    private final int id;
    private final long runAt;
    private final Runnable job;

    Task(int id, long runAt, Runnable job) {
        this.id = id;
        this.runAt = runAt;
        this.job = job;
    }

    void execute() {
        job.run();
    }

    public int compareTo(Task other) {
        return Long.compare(this.runAt, other.runAt);
    }

    long getRunAt() {
        return runAt;
    }
}

class TaskQueue {
    private final PriorityQueue<Task> pq = new PriorityQueue<>();

    synchronized void add(Task task) {
        pq.offer(task);
        notify();
    }

    synchronized Task peek() {
        return pq.peek();
    }

    synchronized Task poll() {
        return pq.poll();
    }
}

class Scheduler {
    private final TaskQueue queue = new TaskQueue();
    private final AtomicBoolean running = new AtomicBoolean(true);

    Scheduler() {
        new Worker().start();
    }

    void schedule(Task task) {
        queue.add(task);
    }

    void stop() {
        running.set(false);
    }

    private class Worker extends Thread {
        public void run() {
            while (running.get()) {
                try {
                    Task task;
                    synchronized (queue) {
                        while ((task = queue.peek()) == null) {
                            queue.wait();
                        }

                        long now = System.currentTimeMillis();
                        long delay = task.getRunAt() - now;

                        if (delay > 0) {
                            queue.wait(delay);
                            continue;
                        }

                        queue.poll();
                    }

                    task.execute();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

class Main {

    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler();

        // Random input tasks
        scheduler.schedule(new Task(
                1,
                System.currentTimeMillis() + 2000,
                () -> System.out.println("Task A executed")
        ));

        scheduler.schedule(new Task(
                2,
                System.currentTimeMillis() + 1000,
                () -> System.out.println("Task B executed")
        ));
    }
}

