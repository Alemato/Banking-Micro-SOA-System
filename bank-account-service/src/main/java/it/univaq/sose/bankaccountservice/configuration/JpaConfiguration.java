package it.univaq.sose.bankaccountservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class for enabling JPA auditing.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfiguration {
}
