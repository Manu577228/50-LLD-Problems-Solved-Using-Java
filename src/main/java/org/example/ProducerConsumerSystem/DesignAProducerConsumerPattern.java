package org.example.ProducerConsumerSystem;

//Design a Producer–Consumer system where producers generate data and
//consumers process data concurrently using a shared bounded buffer.
//The system must ensure thread safety, no data loss, and proper
//synchronization between producers and consumers.

import java.util.LinkedList;
import java.util.Queue;

class Buffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    Buffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }

        queue.add(item);

        notifyAll();
    }

    public synchronized int take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        int item = queue.poll();

        notifyAll();

        return item;
    }
}

class Producer implements Runnable {
    private final Buffer buffer;

    Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        int value = 1;
        try {
            while (true) {
                buffer.put(value);
                System.out.printf("Produced: " + value);

                value++;
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final Buffer buffer;

    Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        int value = 1;
        try {
            while (true) {
                int item = buffer.take();
                System.out.printf("Consumed: " + item);

                Thread.sleep(800);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Main {

    public static void main(String[] args) {

        Buffer buffer = new Buffer(5); // random buffer size = 5

        Thread producer = new Thread(new Producer(buffer));
        Thread consumer = new Thread(new Consumer(buffer));

        producer.start();
        consumer.start();
    }
}
