package it.univaq.sose.loanserviceprosumer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class for enabling JPA Auditing.
 * <p>
 * This configuration class is responsible for enabling JPA Auditing in the Spring application.
 * JPA Auditing allows automatic population of auditing-related fields like createdDate,
 * lastModifiedDate, createdBy, and lastModifiedBy in entity classes.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfiguration {
}
