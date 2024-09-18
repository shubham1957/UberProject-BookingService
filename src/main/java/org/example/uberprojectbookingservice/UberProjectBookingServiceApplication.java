package org.example.uberprojectbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableDiscoveryClient
@EntityScan("org.example.uberprojectentityservice.models")
public class UberProjectBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberProjectBookingServiceApplication.class, args);
    }

}
