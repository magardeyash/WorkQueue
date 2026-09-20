package com.workqueue.worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workqueue.worker.model.Metrics;
import com.workqueue.worker.model.Task;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class WorkerService {

    private static final String QUEUE_KEY = "task_queue";
    private static final int NUM_WORKERS = 3;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TaskProcessorService taskProcessor;

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private ObjectMapper objectMapper;

    private final AtomicLong totalJobsInQueue = new AtomicLong(0);
    private final AtomicInteger jobsDone = new AtomicInteger(0);
    private final AtomicInteger jobsFailed = new AtomicInteger(0);

    private ExecutorService executor;
    private volatile boolean running = true;

    @PostConstruct
    public void startWorkers() {
        executor = Executors.newFixedThreadPool(NUM_WORKERS);
        for (int i = 0; i < NUM_WORKERS; i++) {
            executor.submit(this::runWorkerLoop);
        }
        log.info("Started {} worker threads listening on queue: {}", NUM_WORKERS, QUEUE_KEY);
    }

    @PreDestroy
    public void stopWorkers() {
        running = false;
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private void runWorkerLoop() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                // Blocking pop with a 2-second timeout to allow clean thread shutdown
                String jsonTask = redisTemplate.opsForList().leftPop(QUEUE_KEY, Duration.ofSeconds(2));

                if (jsonTask != null) {
                    Task task = objectMapper.readValue(jsonTask, Task.class);
                    Long currentSize = redisTemplate.opsForList().size(QUEUE_KEY);
                    totalJobsInQueue.set(currentSize != null ? currentSize : 0);

                    try {
                        taskProcessor.processTask(task);
                        jobsDone.incrementAndGet();
                        loggerService.logSuccess(task);
                    } catch (Exception e) {
                        jobsFailed.incrementAndGet();
                        loggerService.logFailure(task, e);

                        int retriesLeft = task.getRetries() - 1;
                        if (retriesLeft > 0) {
                            task.setRetries(retriesLeft);
                            String reenqueueJson = objectMapper.writeValueAsString(task);
                            redisTemplate.opsForList().rightPush(QUEUE_KEY, reenqueueJson);
                            log.info("Re-enqueued failed task. Retries remaining: {}", retriesLeft);
                        }
                    }
                }
            } catch (Exception e) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                log.error("Error in worker loop: {}", e.getMessage());
            }
        }
    }

    public Metrics getMetrics() {
        Long queueSize = redisTemplate.opsForList().size(QUEUE_KEY);
        long currentQueueSize = (queueSize != null) ? queueSize : 0;
        return new Metrics(currentQueueSize, jobsDone.get(), jobsFailed.get());
    }
}
