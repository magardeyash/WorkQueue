package com.workqueue.worker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String type;
    private Map<String, Object> payload;
    private int retries;
}
