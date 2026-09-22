package com.workqueue.worker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WorkQueue — Worker API")
                        .description("Distributed Background Task Processing System. " +
                                "Worker service runs 3 concurrent threads that process tasks from the Redis queue. " +
                                "Use /metrics to monitor queue size, completed jobs, and failed jobs in real time.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Yash Magarde")
                                .url("https://linkedin.com/in/yash-magarde")));
    }
}
