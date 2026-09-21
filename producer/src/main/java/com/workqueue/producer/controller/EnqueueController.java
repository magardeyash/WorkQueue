package com.workqueue.producer.controller;

import com.workqueue.producer.model.Task;
import com.workqueue.producer.service.ProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Producer", description = "Enqueue background tasks into the Redis queue")
public class EnqueueController {

    @Autowired
    private ProducerService producerService;

    @Operation(
        summary = "Add a task to the queue",
        description = "Accepts a task object and pushes it to the Redis queue for async background processing by the Worker service.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Task.class),
                examples = {
                    @ExampleObject(name = "Send Email", value = """
                        {
                          "type": "send_email",
                          "retries": 3,
                          "payload": {
                            "to": "user@example.com",
                            "subject": "Welcome to WorkQueue!"
                          }
                        }
                        """),
                    @ExampleObject(name = "Resize Image", value = """
                        {
                          "type": "resize_image",
                          "retries": 2,
                          "payload": {
                            "new_x": 1920,
                            "new_y": 1080
                          }
                        }
                        """),
                    @ExampleObject(name = "Generate PDF", value = """
                        {
                          "type": "generate_pdf",
                          "retries": 1,
                          "payload": {
                            "document_id": 999
                          }
                        }
                        """)
                }
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Task successfully added to queue"),
            @ApiResponse(responseCode = "400", description = "Invalid task — missing required fields"),
            @ApiResponse(responseCode = "500", description = "Failed to connect to Redis")
        }
    )
    @PostMapping("/enqueue")
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
