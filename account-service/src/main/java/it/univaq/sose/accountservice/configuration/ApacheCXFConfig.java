package it.univaq.sose.accountservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.openapi.OpenApiFeature;
import org.apache.cxf.jaxrs.swagger.ui.SwaggerUiConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Apache CXF.
 */
@Configuration
public class ApacheCXFConfig {

    @Value("${cxf.path}")
    private String cxfPath;

    /**
     * Configures the logging feature for CXF.
     *
     * @return the logging feature
     */
    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);  // Enables pretty logging for better readability
        return loggingFeature;
    }

    /**
     * Configures the Jackson JSON provider for CXF.
     *
     * @return the Jackson JSON provider
     */
    @Bean
    public JacksonJsonProvider jsonProvider() {
        // Create and configure the ObjectMapper for JSON processing
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());// Register module for Java 8 date and time API
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);// Disable writing dates as timestamps
        // Create and return the JacksonJsonProvider with the configured ObjectMapper
        return new JacksonJsonProvider(objectMapper);
    }

    /**
     * Configures the OpenAPI feature for CXF.
     *
     * @return the OpenAPI feature
     */
    @Bean
    public OpenApiFeature createOpenApiFeature() {
        // Create and configure the OpenApiFeature
        final OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true);
        openApiFeature.setTitle("Account Service for Banking Micro-SOA System");
        openApiFeature.setContactName("The Banking Micro-SOA System team");
        openApiFeature.setDescription("This is Account Service for Banking Micro-SOA System. Uses Apache CXF and Spring Boot on JAX-RS.");
        openApiFeature.setVersion("0.0.1-SNAPSHOT");
        openApiFeature.setSwaggerUiConfig(
                new SwaggerUiConfig()
                        .url(cxfPath + "/openapi.json").queryConfigEnabled(false));
        return openApiFeature;
    }
}
