package org.example.ThreadPool;

//Design a Thread Pool that manages a fixed number of worker threads
//to execute submitted tasks concurrently.
//Tasks should be queued, picked by idle threads, executed safely,
//and the system should shut down gracefully.

import java.util.LinkedList;
import java.util.Queue;

class ThreadPool {
    private final TaskQueue taskQueue;
    private final WorkerThread[] workers;
    private volatile boolean isShutdown = false;

    ThreadPool(int poolSize) {
        this.taskQueue = new TaskQueue();
        workers = new WorkerThread[poolSize];

        for (int i = 0; i < poolSize; i++) {
            workers[i] = new WorkerThread(taskQueue);
            workers[i].start();
        }
    }

    public void submit(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shutdown");
        }

        taskQueue.put(task);
    }

    public void shutdown() {
        isShutdown = true;
        for (WorkerThread worker : workers) {
            worker.stopWorker();
        }
    }
}

class WorkerThread extends Thread {
    private final TaskQueue taskQueue;
    private volatile boolean isRunning = true;

    public WorkerThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run() {
        while (isRunning) {
            try {
                Runnable task = taskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopWorker() {
        isRunning = false;
        this.interrupt();
    }
}

class TaskQueue {
    private final Queue<Runnable> queue = new LinkedList<>();

    public synchronized void put(Runnable task) {
        queue.add(task);
        notify();
    }

    public synchronized Runnable take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        return queue.poll();
    }
}

class ThreadPoolDemo {

    public static void main(String[] args) {

        // Create thread pool with 3 workers
        ThreadPool pool = new ThreadPool(3);

        // Submit 5 tasks
        for (int i = 1; i <= 5; i++) {
            int taskId = i;

            pool.submit(() -> {
                System.out.println("Task " + taskId +
                        " executed by " + Thread.currentThread().getName());
            });
        }

        // Shutdown pool
        pool.shutdown();
    }
}