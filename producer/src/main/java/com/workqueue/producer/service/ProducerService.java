package com.workqueue.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workqueue.producer.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private static final String QUEUE_KEY = "task_queue";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void enqueue(Task task) throws JsonProcessingException {
        String jsonTask = objectMapper.writeValueAsString(task);
        redisTemplate.opsForList().rightPush(QUEUE_KEY, jsonTask);
        Long size = redisTemplate.opsForList().size(QUEUE_KEY);
        System.out.println("Enqueued task of type '" + task.getType() + "'. Current queue length: " + size);
    }
}
