package com.workqueue.producer.controller;

import com.workqueue.producer.model.Task;
import com.workqueue.producer.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enqueue")
public class EnqueueController {

    @Autowired
    private ProducerService producerService;

    @PostMapping
    public ResponseEntity<String> enqueue(@RequestBody Task task) {
        if (task == null || task.getType() == null || task.getType().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Task type is required");
        }

        if ("send_email".equalsIgnoreCase(task.getType())) {
            if (task.getPayload() == null ||
                task.getPayload().get("to") == null ||
                task.getPayload().get("subject") == null) {
                return ResponseEntity.badRequest().body("to and subject are required for send_email");
            }
        }

        try {
            producerService.enqueue(task);
            return ResponseEntity.ok("Task of type '" + task.getType() + "' added to queue");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to enqueue task: " + e.getMessage());
        }
    }
}
