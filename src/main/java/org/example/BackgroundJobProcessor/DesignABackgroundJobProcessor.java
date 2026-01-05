package org.example.BackgroundJobProcessor;

//Design a Background Job Processor that accepts jobs asynchronously, stores
//them safely, and executes them in the background using worker threads.
//The system should support retry, failure handling, and scalability
//while remaining simple and extensible.

import java.util.concurrent.*;

enum JobStatus {
    PENDING, RUNNING, SUCCESS, FAILED
}

abstract class Job {
    final String id;
    int retriesLeft;
    JobStatus status;

    Job(String id, int retries) {
        this.id = id;
        this.retriesLeft = retries;
        this.status = JobStatus.PENDING;
    }

    abstract void execute() throws Exception;
}

class JobQueue {
    private final BlockingQueue<Job> queue = new LinkedBlockingQueue<>();

    void submit(Job job) {
        queue.add(job);
    }

    Job take() throws InterruptedException {
        return queue.take();
    }
}

class RetryPolicy {
    boolean shouldRetry(Job job) {
        return job.retriesLeft > 0;
    }
}

class Worker implements Runnable {
    private final JobQueue jobQueue;
    private final RetryPolicy retryPolicy;

    Worker(JobQueue jq, RetryPolicy rp) {
        this.jobQueue = jq;
        this.retryPolicy = rp;
    }

    public void run() {
        while (true) {
            try {
                Job job = jobQueue.take();
                job.status = JobStatus.RUNNING;

                job.execute();
                job.status = JobStatus.RUNNING;

                System.out.println("Job " + job.id + " completed");
            } catch (Exception e) {
                System.out.println("Job Failed: " + e.getMessage());
            }
        }
    }
}

class JobProcessor {
    private final ExecutorService executor;

    JobProcessor(int workers, JobQueue jq, RetryPolicy rp) {
        executor = Executors.newFixedThreadPool(workers);
        for (int i = 0; i < workers; i++) {
            executor.submit(new Worker(jq, rp));
        }
    }

    void shutdown() {
        executor.shutdown();
    }
}

class PrintJob extends Job {
    PrintJob(String id) {
        super(id, 2);
    }

    void execute() {
        System.out.println("Executing job: " + id);
    }
}

class Main {
    public static void main(String[] args) {
        JobQueue queue = new JobQueue();
        RetryPolicy policy = new RetryPolicy();

        JobProcessor processor = new JobProcessor(2, queue, policy);

        // Random input jobs
        queue.submit(new PrintJob("JOB-101"));
        queue.submit(new PrintJob("JOB-102"));
        queue.submit(new PrintJob("JOB-103"));
    }
}


