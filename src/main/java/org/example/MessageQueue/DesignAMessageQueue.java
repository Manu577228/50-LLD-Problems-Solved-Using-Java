package org.example.MessageQueue;

//Design an in-memory Message Queue system that allows producers to publish messages and
//consumers to consume them asynchronously.
//The system must support FIFO ordering, thread-safety, and multiple producers and consumers.

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Message {
    private final int id;
    private final String payload;

    public Message(int id, String payload) {
        this.id = id;
        this.payload = payload;
    }

    public int getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }
}

class MessageQueue {
    private final BlockingQueue<Message> queue;

    public MessageQueue(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    public void enqueue(Message message) throws InterruptedException {
        queue.put(message);
        System.out.println("Enqueued: " + message.getPayload());
    }

    public Message dequeue() throws InterruptedException {
        Message message = queue.take();
        System.out.println("Dequeued: " + message.getPayload());
        return message;
    }
}

class Producer implements Runnable {
    private final MessageQueue messageQueue;

    public Producer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                Message message = new Message(i, "Message." + i);
                messageQueue.enqueue(message);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final MessageQueue messageQueue;

    public Consumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                messageQueue.dequeue();
                Thread.sleep(800);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class MessageQueueDemo {

    public static void main(String[] args) {

        // Queue capacity = 3
        MessageQueue queue = new MessageQueue(3);

        // Create producer and consumer threads
        Thread producer = new Thread(new Producer(queue));
        Thread consumer = new Thread(new Consumer(queue));

        // Start threads
        producer.start();
        consumer.start();
    }
}
