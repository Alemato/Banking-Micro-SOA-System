package it.univaq.sose.bancomatservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * Configuration class for enabling JPA Auditing.
 * <p>
 * This configuration class is responsible for enabling JPA Auditing in the Spring application.
 * JPA Auditing allows us to automatically populate auditing-related fields like createdDate,
 * lastModifiedDate, createdBy, and lastModifiedBy in our entity classes.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfiguration {
}
