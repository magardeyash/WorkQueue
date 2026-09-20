package com.workqueue.worker.controller;

import com.workqueue.worker.model.Metrics;
import com.workqueue.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private WorkerService workerService;

    @GetMapping
    public ResponseEntity<Metrics> getMetrics() {
        return ResponseEntity.ok(workerService.getMetrics());
    }
}
