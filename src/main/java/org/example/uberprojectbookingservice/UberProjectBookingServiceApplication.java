package org.example.uberprojectbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("org.example.uberprojectentityservice.models")
public class UberProjectBookingServiceApplication {

    public static void main(String[] args) {
        System.out.println("MySQL Password from Env: " + System.getenv("mysql_password"));

        SpringApplication.run(UberProjectBookingServiceApplication.class, args);
    }

}
