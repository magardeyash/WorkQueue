package com.workqueue.worker.controller;

import com.workqueue.worker.model.Metrics;
import com.workqueue.worker.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
@Tag(name = "Worker Metrics", description = "Monitor the WorkQueue processing statistics in real time")
public class MetricsController {

    @Autowired
    private WorkerService workerService;

    @Operation(
        summary = "Get live worker metrics",
        description = "Returns real-time statistics: number of jobs currently in the Redis queue, total jobs processed successfully, and total jobs that failed.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Live metrics returned successfully",
                content = @Content(schema = @Schema(implementation = Metrics.class))
            )
        }
    )
    @GetMapping
    public ResponseEntity<Metrics> getMetrics() {
        return ResponseEntity.ok(workerService.getMetrics());
    }
}
