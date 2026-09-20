package com.workqueue.worker.service;

import com.workqueue.worker.model.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskProcessorService {

    public void processTask(Task task) throws Exception {
        if (task == null) {
            throw new Exception("Task object is null");
        }
        if (task.getType() == null || task.getType().trim().isEmpty()) {
            throw new Exception("Task type is empty");
        }
        if (task.getPayload() == null) {
            throw new Exception("Payload is empty");
        }

        switch (task.getType().toLowerCase()) {
            case "send_email":
                Thread.sleep(2000); // simulate delay
                System.out.println("Sending email to " + task.getPayload().get("to")
                        + " with subject " + task.getPayload().get("subject"));
                break;

            case "resize_image":
                System.out.println("Resizing image to x: " + task.getPayload().get("new_x")
                        + " y: " + task.getPayload().get("new_y"));
                break;

            case "generate_pdf":
                System.out.println("Generating PDF...");
                break;

            default:
                throw new Exception("Unsupported task type: " + task.getType());
        }
    }
}
