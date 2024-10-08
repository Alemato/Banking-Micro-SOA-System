package it.univaq.sose.bankingoperationsserviceprosumer.configuration;

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
 * Configures Apache CXF for the Banking Operations Service (Prosumer) application.
 * This includes JSON provider, logging, and OpenAPI features.
 */
@Configuration
public class ApacheCXFConfig {
    @Value("${cxf.path}")
    private String cxfPath;

    /**
     * Configures a Jackson JSON provider for use with CXF.
     * This provider handles JSON serialization and deserialization.
     *
     * @return the configured JacksonJsonProvider
     */
    @Bean
    public JacksonJsonProvider jsonProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new JacksonJsonProvider(objectMapper);
    }

    /**
     * Configures a logging feature for CXF to provide detailed logging of requests and responses.
     * The logs are formatted for better readability.
     *
     * @return the configured LoggingFeature
     */
    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true); // Enable pretty logging for better readability
        return loggingFeature;
    }

    /**
     * Configures the OpenAPI feature for CXF, which generates and serves the OpenAPI documentation
     * for the REST endpoints in this service.
     *
     * @return the configured OpenApiFeature
     */
    @Bean
    public OpenApiFeature createOpenApiFeature() {
        final OpenApiFeature openApiFeature = new OpenApiFeature();
        openApiFeature.setPrettyPrint(true); // Enable pretty print for the OpenAPI output
        openApiFeature.setTitle("Banking Operations Service (Prosumer) for Banking Micro-SOA System");
        openApiFeature.setContactName("The Banking Micro-SOA System team");
        openApiFeature.setDescription("This is Banking Operations Service (Prosumer) for Banking Micro-SOA System. Uses Apache CXF and Spring Boot on JAX-RS.");
        openApiFeature.setVersion("0.0.1-SNAPSHOT");
        openApiFeature.setSwaggerUiConfig(
                new SwaggerUiConfig()
                        .url(cxfPath + "/openapi.json").queryConfigEnabled(false));
        return openApiFeature;
    }
}
