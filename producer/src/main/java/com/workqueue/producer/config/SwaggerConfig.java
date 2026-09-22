package com.workqueue.producer.config;

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
                        .title("WorkQueue — Producer API")
                        .description("Distributed Background Task Processing System. " +
                                "Use this endpoint to enqueue tasks like send_email, resize_image, generate_pdf " +
                                "into the Redis queue for async background processing.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Yash Magarde")
                                .url("https://linkedin.com/in/yash-magarde")));
    }
}
