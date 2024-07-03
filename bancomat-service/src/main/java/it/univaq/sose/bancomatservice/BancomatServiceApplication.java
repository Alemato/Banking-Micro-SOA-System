package it.univaq.sose.bancomatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BancomatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancomatServiceApplication.class, args);
    }

}
