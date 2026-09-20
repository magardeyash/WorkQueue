package com.workqueue.worker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metrics {
    private long totalJobsInQueue;
    private int jobsDone;
    private int jobsFailed;
}
