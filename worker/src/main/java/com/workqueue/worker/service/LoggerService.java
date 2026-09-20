package com.workqueue.worker.service;

import com.workqueue.worker.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggerService {

    public void logSuccess(Task task) {
        log.info("SUCCESS: Type={} Payload={} RetriesLeft={}",
                task.getType(), task.getPayload(), task.getRetries());
    }

    public void logFailure(Task task, Exception e) {
        log.error("FAILURE: Type={} Payload={} RetriesLeft={} Error={}",
                task.getType(), task.getPayload(), task.getRetries(), e.getMessage());
    }
}
